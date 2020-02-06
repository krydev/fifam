package ukma.project.fifam.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ukma.project.fifam.models.*;
import ukma.project.fifam.repos.UserRepo;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping(value = "/users")
    public ResponseEntity index() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @GetMapping(value = "/user")
    public ResponseEntity getUser(@RequestParam(value="id") Long id) {
        Optional<User> foundUser = userRepo.findById(id);

        if(foundUser.isPresent()){
            return ResponseEntity.ok(foundUser.get());
        } else {
            return ResponseEntity.badRequest().body("No user with specified id " + id + " found");
        }
    }

    @PostMapping(value = "/user")
    public ResponseEntity addUser(
            @RequestParam(value="email") String email,
            @RequestParam(value="password") String pass,
            @RequestParam(value="balance") String balance) {
        return ResponseEntity.ok(userRepo.save(new User(email, pass, balance)));
    }

    @PutMapping(value = "/user")
    public ResponseEntity updateUser(
            @RequestParam(value="id") Long id,
            @RequestParam(value="email") String email,
            @RequestParam(value="password") String pass,
            @RequestParam(value="balance") String balance){
        Optional<User> optionalUser = userRepo.findById(id);
        if(!optionalUser.isPresent()){
            return ResponseEntity.badRequest().body("No user with specified id " + id + " found");
        }

        User foundUser = optionalUser.get();
        foundUser.setEmail(email);
        foundUser.setPassword(pass);
        foundUser.setBalance(balance);

        return ResponseEntity.ok(userRepo.save(foundUser));
    }

    @DeleteMapping(value = "/user")
    public ResponseEntity removeUser(@RequestParam(value="id") Long id) {
        userRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}