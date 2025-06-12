package com.coffee.coffee_diary.controllers;

import com.coffee.coffee_diary.containers.GetLoginPasswordResult;
import com.coffee.coffee_diary.repositories.UserRepository;
import com.coffee.coffee_diary.schemas.users.LoginUserInput;
import com.coffee.coffee_diary.schemas.users.RegisterUserInput;
import com.coffee.coffee_diary.security.AccessToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public ResponseEntity<byte[]> registerUser(@RequestBody byte[] body) {
        // Parse request body
        RegisterUserInput data = RegisterUserInput.fromRequestBody(body);
        if (data.getError() != null) {
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(data.getError().getBytes(), responseHeader, HttpStatus.BAD_REQUEST);
        }

        // Check if user with the given email already exists
        boolean userExists = this.userRepository.existsWithEmail(data.getEmail());
        if (userExists) {
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>("User with email already exists".getBytes(), responseHeader , HttpStatus.CONFLICT);
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(data.getPassword(), BCrypt.gensalt());

        // Register user into the database
        this.userRepository.addUser(data.getEmail(), hashedPassword);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<byte[]> loginUser(@RequestBody byte[] body) {
        // Initialize response header
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setContentType(MediaType.TEXT_PLAIN);

        // Parse request body
        LoginUserInput data = LoginUserInput.fromRequestBody(body);
        if (data.getError() != null) {
            return new ResponseEntity<>(data.getError().getBytes(), responseHeader, HttpStatus.BAD_REQUEST);
        }

        // Look up user in database
        GetLoginPasswordResult loginPasswordResult = this.userRepository.getLoginPassword(data.getEmail());
        if (loginPasswordResult == null) {
            return new ResponseEntity<>("User with email not found".getBytes(), responseHeader, HttpStatus.BAD_REQUEST);
        }

        // Verify user password
        if (!BCrypt.checkpw(data.getPassword(), loginPasswordResult.password())) {
            return new ResponseEntity<>("Incorrect password".getBytes(), responseHeader, HttpStatus.BAD_REQUEST);
        }

        // Create access token and write it to the response body
        AccessToken accessToken = new AccessToken(loginPasswordResult.userID(), Instant.now().getEpochSecond() + 3600);
        return new ResponseEntity<>(accessToken.encode("secretKey"), responseHeader, HttpStatus.OK);
    }
}
