package ukma.project.fifam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ukma.project.fifam.dtos.auth.AuthDto;
import ukma.project.fifam.dtos.journals.JournalCreateDto;
import ukma.project.fifam.models.Category;
import ukma.project.fifam.models.Journal;
import ukma.project.fifam.models.User;
import ukma.project.fifam.repos.CategoryRepo;
import ukma.project.fifam.repos.JournalRepo;
import ukma.project.fifam.repos.UserRepo;

import java.util.Optional;

@RestController
public class JournalController {
    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping(value = "/journal")
    public ResponseEntity<?> getJournalRecords(@RequestAttribute(value = "userId") Long userId){
        Optional<User> currUser = userRepo.findById(userId);
        return ResponseEntity.ok(journalRepo.findJournalsByUserId(currUser.get()));
    }

    @PostMapping(value = "/journal")
    public ResponseEntity<?> createJournalRecord(@RequestAttribute(value = "userId") Long userId,
                                           @Validated @RequestBody JournalCreateDto dto){

        Optional<User> user = userRepo.findById(userId);
        Category category = null;
        if (dto.categoryId != null){
            Optional<Category> cat = categoryRepo.findById(dto.categoryId);
            if (!cat.isPresent()){
                return ResponseEntity.badRequest().body("No category with specified id " + dto.categoryId + " found");
            }
            category = cat.get();
        }
        Journal journal = new Journal(user.get(), category, dto.recordDate, dto.sum, dto.desc, dto.currBalance);
        return ResponseEntity.ok(journalRepo.save(journal));
    }
}
