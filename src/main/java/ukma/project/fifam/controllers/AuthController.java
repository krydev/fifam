package ukma.project.fifam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ukma.project.fifam.dtos.auth.AuthDto;
import ukma.project.fifam.models.User;
import ukma.project.fifam.repos.UserRepo;
import ukma.project.fifam.utils.JwtUtil;

import java.util.Optional;

@RestController
public class AuthController {
    @Autowired
    private UserRepo userRepo;

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
        return new ResponseEntity<>(jwtUtil.generateToken(userForReal), HttpStatus.OK);
    }
}
