package com.blockchain.csr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/testAuth")
    public ResponseEntity<String> test(Authentication authentication) {
        return ResponseEntity.ok("Hello, " + authentication.getName() + "! You have successfully authenticated.");
    }
}
