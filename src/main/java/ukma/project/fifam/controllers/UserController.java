package ukma.project.fifam.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ukma.project.fifam.models.*;
import ukma.project.fifam.repos.JournalRepo;
import ukma.project.fifam.repos.UserRepo;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JournalRepo journalRepo;

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
        currUser.increaseBalance(addSum);
        Journal journalRecord = new Journal(currUser,
                null, Instant.now().getEpochSecond(), addSum,
                "Income", currUser.getBalance());
        return ResponseEntity.ok(journalRepo.save(journalRecord));
    }


    @DeleteMapping(value = "/profile")
    public ResponseEntity<String> removeUser(@RequestAttribute(value="userId") Long userId) {
        if(!userRepo.findById(userId).isPresent()){
            return ResponseEntity.notFound().build();
        }
        userRepo.deleteById(userId);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}