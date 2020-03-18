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
import ukma.project.fifam.models.BalanceFiller;
import ukma.project.fifam.models.Journal;
import ukma.project.fifam.models.PeriodicPays;
import ukma.project.fifam.models.User;
import ukma.project.fifam.repos.BalanceFillerRepo;
import ukma.project.fifam.repos.JournalRepo;
import ukma.project.fifam.repos.PeriodicPaysRepo;
import ukma.project.fifam.repos.UserRepo;
import ukma.project.fifam.utils.JwtUtil;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
//        journalRepo.updateJournalFromBalanceFiller("daily","weekly", "biweekly", "monthly", "yearly", userForReal.getId());
//        journalRepo.updateJournalFromPeriodicPays("daily","weekly", "biweekly", "monthly", "yearly", userForReal.getId());
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

    public void updateJournalFromBalanceFiller(User user)
    {
        long currentTime = Instant.now().getEpochSecond();
        Optional<Journal> lastJournal = journalRepo.findLastUsersJournalLog(user.getId());
        double lastBalance = 0;
        if (lastJournal.isPresent()){
            lastBalance = Double.parseDouble(lastJournal.get().getCurrBalance());
        }


        Optional<List<BalanceFiller>> balanceFillersOptional = balanceFillerRepo.findBalanceFillersByUserId(user);
        if(balanceFillersOptional.isPresent()){
            List<BalanceFiller> balanceFillers = balanceFillersOptional.get();
            for (BalanceFiller bf: balanceFillers) {
                lastBalance = processBalanceFiller(bf, user, lastBalance, currentTime);
            }
        }

        Optional<List<PeriodicPays>> periodicPaysOptional = periodicPaysRepo.findPeriodicPaysByUserId(user);
        if (periodicPaysOptional.isPresent()){
            List<PeriodicPays> periodicPays = periodicPaysOptional.get();
            for (PeriodicPays pp : periodicPays){
                lastBalance = processPeriodicPay(pp, user, lastBalance, currentTime);
            }
        }
    }
}
