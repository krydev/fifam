package ukma.project.fifam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ukma.project.fifam.Frequency;
import ukma.project.fifam.dtos.categories.CategoryDto;
import ukma.project.fifam.dtos.categories.ExpenseDto;
import ukma.project.fifam.models.Category;
import ukma.project.fifam.models.Journal;
import ukma.project.fifam.models.User;
import ukma.project.fifam.repos.CategoryRepo;
import ukma.project.fifam.repos.JournalRepo;
import ukma.project.fifam.repos.UserRepo;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
public class CategoryController {
    @Autowired
    private CategoryRepo catRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JournalRepo journalRepo;

    @GetMapping(value = "/categories")
    public ResponseEntity<?> getCategories(@RequestAttribute(value = "userId") Long userId){
        Optional<User> currUser = userRepo.findById(userId);
        return ResponseEntity.ok(currUser.get().getCategories());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById(@RequestAttribute(value = "userId") Long userId,
                                          @PathVariable String id){
        Optional<User> currUser = userRepo.findById(userId);
        return findCategoryById(currUser.get(), id);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestAttribute(value = "userId") Long userId,
                                            @Validated @RequestBody CategoryDto body){
        Optional<User> currUser = userRepo.findById(userId);
        String name = body.name;
        String budget = body.budget;
        Frequency freq = Frequency.valueOf(body.frequency);
        User user = currUser.get();
        long lastPayDate = Instant.now().getEpochSecond();

        return ResponseEntity.ok(catRepo.save(new Category(name, budget, freq, lastPayDate, user)));
    }

    @PostMapping("/categories/{id}/expenses")
    public ResponseEntity<?> addExpense(@RequestAttribute(value = "userId") Long userId,
                                        @PathVariable String id,
                                        @Validated @RequestBody ExpenseDto body){
        User currUser = userRepo.findById(userId).get();
        ResponseEntity resp = findCategoryById(currUser, id);
        if (!resp.getStatusCode().equals(HttpStatus.OK)) return resp;
        Category category = (Category) resp.getBody();
        assert category != null;

        currUser.decreaseBalance(body.sum);
        category.increaseCurrentExpenses(body.sum);

        Journal expenseRecord = new Journal(currUser,
                category, body.recordDate, "-" + body.sum,
                body.desc, currUser.getBalance());

        //committing changes
        userRepo.save(currUser);
        catRepo.save(category);
        return ResponseEntity.ok(journalRepo.save(expenseRecord));
    }


    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@RequestAttribute(value = "userId") Long userId,
                                            @PathVariable String id,
                                            @RequestBody Map<String, String> body){
        Optional<User> currUser = userRepo.findById(userId);
        ResponseEntity resp = findCategoryById(currUser.get(), id);
        if (!resp.getStatusCode().equals(HttpStatus.OK)) return resp;
        Category category = (Category) resp.getBody();
        assert category != null;

        String name = body.get("name");
        if (name != null) category.setName(name);
        String budget = body.get("budget");
        if (budget != null) category.setBudget(budget);
        String currExpenses = body.get("currentExpenses");
        if (currExpenses != null) category.increaseCurrentExpenses(currExpenses);
        String freq = body.get("frequency");
        if (freq != null) category.setFreq(Frequency.valueOf(freq));

        return ResponseEntity.ok(catRepo.save(category));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@RequestAttribute(value = "userId") Long userId,
                                            @PathVariable String id){
        Optional<User> currUser = userRepo.findById(userId);
        ResponseEntity resp = findCategoryById(currUser.get(), id);
        if (!resp.getStatusCode().equals(HttpStatus.OK)) return resp;

        Category category = (Category) resp.getBody();
        catRepo.delete(category);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> findCategoryById(User currUser, String id){
        Long catId = Long.parseLong(id);
        Optional<Category> category = catRepo.findById(catId);
        if (!category.isPresent()) return ResponseEntity.badRequest().body("Category not found");
        Category categoryFound = category.get();
        if (!categoryFound.getUser().equals(currUser)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(categoryFound);
    }

}
