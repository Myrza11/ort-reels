package org.example.aktanoopproject.controller;

import org.example.aktanoopproject.config.RevokedTokenService;
import org.example.aktanoopproject.dto.LoginDTO;
import org.example.aktanoopproject.dto.UserDTO;
import org.example.aktanoopproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RevokedTokenService revokedTokenService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO student) {
        authService.register(student);
        return ResponseEntity.ok("Регистрация успешна. Проверьте email.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        return ResponseEntity.ok(Collections.singletonMap("token", authService.authenticate(login.getEmail(), login.getPassword())));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        revokedTokenService.revokeToken(jwt);
        return ResponseEntity.ok("Logged out successfully");
    }

}
