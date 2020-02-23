package ukma.project.fifam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ukma.project.fifam.dtos.auth.AuthDto;
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

    @GetMapping(value = "/journal")
    public ResponseEntity<?> getJournalRecords(@RequestAttribute(value = "userId") Long userId){
        Optional<User> currUser = userRepo.findById(userId);
        return ResponseEntity.ok(journalRepo.findJournalsByUserId(currUser.get()));
    }

}
