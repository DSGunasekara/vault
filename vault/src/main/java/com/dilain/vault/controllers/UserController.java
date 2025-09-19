package com.dilain.vault.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/users")
    public ResponseEntity<String> getUsers() {
        return ResponseEntity.ok("Hello world");
    }
    
}
