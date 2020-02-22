package ukma.project.fifam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ukma.project.fifam.Frequency;
import ukma.project.fifam.models.Category;
import ukma.project.fifam.models.User;
import ukma.project.fifam.repos.CategoryRepo;
import ukma.project.fifam.repos.UserRepo;
import ukma.project.fifam.utils.JwtUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CategoryController {
    @Autowired
    private CategoryRepo catRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

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
                                            @RequestBody Map<String, String> body){
        Optional<User> currUser = userRepo.findById(userId);
        String name = body.get("name");
        String budget = body.get("budget");
        Frequency freq = Frequency.valueOf(body.get("frequency"));
        User user = currUser.get();

        return ResponseEntity.ok(catRepo.save(new Category(name, budget, freq, user)));
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
        if (currExpenses != null) category.setCurrentExpenses(currExpenses);
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

    private Optional<User> getCurrentUser(String header){
        String userId = jwtUtil.getUserIdFromToken(header);
        return userRepo.findById(Long.parseLong(userId));
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
