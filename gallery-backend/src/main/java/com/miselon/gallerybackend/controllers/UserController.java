package com.miselon.gallerybackend.controllers;

import com.miselon.gallerybackend.model.requests.RegisterUserRequest;
import com.miselon.gallerybackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Boolean> checkIfUserExists(@RequestParam("username") String username){
        if(this.userService.checkIfUserExists(username)) return ResponseEntity.ok(true);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

    @GetMapping(path = "/emails")
    public ResponseEntity<Boolean> checkIfEmailIsTaken(@RequestParam("email") String email){
        if(this.userService.checkIfEmailIsTaken(email)) return ResponseEntity.ok(true);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest registerUserRequest){
        this.userService.register(registerUserRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping(path = "/confirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam String token){
        this.userService.confirmRegistration(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
