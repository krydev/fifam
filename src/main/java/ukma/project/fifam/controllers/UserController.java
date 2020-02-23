package ukma.project.fifam.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ukma.project.fifam.dtos.journals.JournalCreateDto;
import ukma.project.fifam.models.*;
import ukma.project.fifam.repos.UserRepo;

import java.time.ZonedDateTime;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JournalController journalController;

    @GetMapping(value = "/profile")
    public ResponseEntity<?> getProfile(@RequestAttribute(value = "userId") Long userId) {
        return ResponseEntity.ok(userRepo.findById(userId).get());
    }

    @PostMapping(value = "/newPassword")
    public ResponseEntity<?> changePassword(@RequestAttribute(value = "userId") Long userId,
                                     @RequestBody Map<String, String> body){
        User currUser = userRepo.findById(userId).get();
        String oldPass = body.get("oldPassword");
        String newPass = body.get("newPassword");

        if (!oldPass.equals(currUser.getPassword()))
            return ResponseEntity.badRequest().body("Incorrect password");

        currUser.setPassword(newPass);
        return ResponseEntity.ok(userRepo.save(currUser));
    }

    @PutMapping(value = "/profile")
    public ResponseEntity<?> updateBalance(@RequestAttribute(value="userId") Long userId,
                                           @RequestBody Map<String, String> body){
        User currUser = userRepo.findById(userId).get();
        String addSum = body.get("addSum");
        currUser.addToBalance(addSum);
        JournalCreateDto dto = new JournalCreateDto(
                null, ZonedDateTime.now().toLocalDateTime(), addSum,
                "Salary", currUser.getBalance());
        return journalController.createJournalRecord(userId, dto);
    }


    @DeleteMapping(value = "/profile")
    public ResponseEntity<?> removeUser(@RequestAttribute(value="userId") Long userId) {
        userRepo.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}