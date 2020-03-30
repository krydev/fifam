package ukma.project.fifam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ukma.project.fifam.Frequency;
import ukma.project.fifam.dtos.auth.AuthDto;
import ukma.project.fifam.dtos.fillers.PeriodicBalanceFiller;
import ukma.project.fifam.dtos.fillers.PeriodicFillerType;
import ukma.project.fifam.models.*;
import ukma.project.fifam.repos.*;
import ukma.project.fifam.utils.JwtUtil;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

@RestController
public class AuthController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private BalanceFillerRepo balanceFillerRepo;

    @Autowired
    private PeriodicPaysRepo periodicPaysRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@Validated @RequestBody AuthDto dto){
        User user = userRepo.save(new User(dto.email, dto.password, "0"));
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@Validated @RequestBody AuthDto dto){
        Optional<User> user = userRepo.findUserByEmail(dto.email);
        if (!user.isPresent()){
            return ResponseEntity.badRequest().body("No user with specified email " + dto.email + " found");
        }

        User userForReal = user.get();
        if (!userForReal.getPassword().equals(dto.password)){
            return ResponseEntity.badRequest().body("Wrong password");
        }
        String t = jwtUtil.generateToken(userForReal);
        jwtUtil.validateToken(t, userForReal);
        updateJournalFromBalanceFiller(userForReal);
        return new ResponseEntity<>(jwtUtil.generateToken(userForReal), HttpStatus.OK);
    }

    public long getSecondsByFrequency(Frequency frequency){
        switch (frequency){
            case DAILY:
                return 86400;
            case WEEKLY:
                return 86400 * 7;
            case BIWEEKLY:
                return 86400 * 14;
            case MONTHLY:
                return (long) (86400 * 30.4375);
            case YEARLY:
                return (long) (86400 * 365.25);
            default:
                return 0;
        }
    }

    public double processBalanceFiller(BalanceFiller bf, User user, double currentBalance, long currentTime){
        long secondsPerPeriod = getSecondsByFrequency(bf.getFreq());
        double moneyToAddPerPeriod = Double.parseDouble(bf.getSum());

        long time = bf.getLastPayDate();
        boolean updated = false;
        while (time + secondsPerPeriod <= currentTime){
            Journal j = new Journal();
            j.setCurrBalance(String.valueOf(currentBalance + moneyToAddPerPeriod));
            j.setDesc("Payment from balance filler");
            j.setRecordDate(time + secondsPerPeriod);
            j.setSum(bf.getSum());
            j.setUser(user);
            journalRepo.save(j);
            time += secondsPerPeriod;
            currentBalance += moneyToAddPerPeriod;
            updated = true;
        }
        if (updated){
            bf.setLastPayDate(time);
            balanceFillerRepo.save(bf);
        }
        return currentBalance;
    }

    public double processPeriodicPay(PeriodicPays pp, User user, double currentBalance, long currentTime){
        long secondsPerPeriod = getSecondsByFrequency(pp.getFreq());
        double moneyToAddPerPeriod = Double.parseDouble(pp.getSum());

        long time = pp.getLastPayDate();
        boolean updated = false;
        while (time + secondsPerPeriod <= currentTime){
            Journal j = new Journal();
            j.setCurrBalance(String.valueOf(currentBalance + moneyToAddPerPeriod));
            j.setDesc(pp.getName());
            j.setRecordDate(time + secondsPerPeriod);
            j.setSum(pp.getSum());
            j.setCategory(pp.getCategory());
            j.setUser(user);
            journalRepo.save(j);
            time += secondsPerPeriod;
            currentBalance += moneyToAddPerPeriod;
            updated = true;
        }
        if (updated){
            pp.setLastPayDate(time);
            periodicPaysRepo.save(pp);
        }
        return currentBalance;
    }

    private PeriodicBalanceFiller updatePeriodicBalanceFiller(PeriodicBalanceFiller balanceFiller, User user, long currentTime, double lastBalance){
        long secondsPerPeriod = getSecondsByFrequency(balanceFiller.frequency);

        long time = balanceFiller.lastPayDate;
        balanceFiller.lastBalance = lastBalance;
        if (time + secondsPerPeriod <= currentTime){
            Journal j = new Journal();
            j.setCurrBalance(String.valueOf(lastBalance + balanceFiller.sum));
            j.setDesc(balanceFiller.name);
            j.setRecordDate(time + secondsPerPeriod);
            j.setSum(balanceFiller.sum.toString());
            j.setCategory(balanceFiller.category);
            j.setUser(user);
            journalRepo.save(j);
            balanceFiller.lastBalance += balanceFiller.sum;
            balanceFiller.lastPayDate = time + secondsPerPeriod;
            if (balanceFiller.type == PeriodicFillerType.BALANCE_FILLER){
                Optional<BalanceFiller> bf = balanceFillerRepo.findById(balanceFiller.id);
                if (bf.isPresent()){
                    bf.get().setLastPayDate(balanceFiller.lastPayDate);
                    balanceFillerRepo.save(bf.get());
                }
            }else if (balanceFiller.type == PeriodicFillerType.PERIODIC_PAY){
                Optional<PeriodicPays> pp = periodicPaysRepo.findById(balanceFiller.id);
                if (pp.isPresent()){
                    pp.get().setLastPayDate(balanceFiller.lastPayDate);
                    periodicPaysRepo.save(pp.get());
                }
            }
        }
        return balanceFiller;
    }

    public void updateJournalFromPriorityQueue(PriorityQueue<PeriodicBalanceFiller> queue, User user, long currentTime, double lastBalance){
        while (!queue.isEmpty()){
            PeriodicBalanceFiller filler = queue.poll();
            filler = updatePeriodicBalanceFiller(filler, user, currentTime, lastBalance);
            if (filler.lastPayDate + getSecondsByFrequency(filler.frequency) <= currentTime){
                queue.add(filler);
            }
            lastBalance = filler.lastBalance;
        }
    }

    public void updateJournalFromBalanceFiller(User user)
    {
        long currentTime = Instant.now().getEpochSecond();
        Optional<Journal> lastJournal = journalRepo.findLastUsersJournalLog(user.getId());
        double lastBalance = 0;
        if (lastJournal.isPresent()){
            lastBalance = Double.parseDouble(lastJournal.get().getCurrBalance());
        }

        // collect balance fillers in priority queue
        Optional<List<BalanceFiller>> balanceFillersOptional = balanceFillerRepo.findBalanceFillersByUserId(user);
        PriorityQueue<PeriodicBalanceFiller> queue = new PriorityQueue<>(PeriodicBalanceFiller.GetComparator());
        if (balanceFillersOptional.isPresent()){
            List<BalanceFiller> balanceFillers = balanceFillersOptional.get();
            for (BalanceFiller bf : balanceFillers){
                queue.add(new PeriodicBalanceFiller(PeriodicFillerType.BALANCE_FILLER, bf.getSum(), bf.getFreq(), bf.getLastPayDate(), "Payment from balance filler", null, bf.getId()));
            }
        }
        // collect periodic pays in priority queue
        Optional<List<PeriodicPays>> periodicPaysOptional = periodicPaysRepo.findPeriodicPaysByUserId(user);
        if (periodicPaysOptional.isPresent()) {
            List<PeriodicPays> periodicPays = periodicPaysOptional.get();
            for (PeriodicPays pp : periodicPays) {
                queue.add(new PeriodicBalanceFiller(PeriodicFillerType.PERIODIC_PAY, pp.getSum(), pp.getFreq(), pp.getLastPayDate(), pp.getName(), pp.getCategory(), pp.getId()));
            }
        }
        updateJournalFromPriorityQueue(queue, user, currentTime, lastBalance);

        // reset expenses in categories
        Optional<List<Category>> categoriesOptional = categoryRepo.findCategoriesByUserId(user);
        if (categoriesOptional.isPresent()){
            List<Category> categories = categoriesOptional.get();
            for(Category category : categories){
                long timeInPeriod = getSecondsByFrequency(category.getFreq());
                boolean hasToBeUpdated = false;
                while (category.getLastPayDate() + timeInPeriod < currentTime){
                    category.setLastPayDate(category.getLastPayDate() + timeInPeriod);
                    category.resetCurrentExpenses();
                    hasToBeUpdated = true;
                }
                if (hasToBeUpdated){
                    categoryRepo.save(category);
                }
            }
        }
    }
}
