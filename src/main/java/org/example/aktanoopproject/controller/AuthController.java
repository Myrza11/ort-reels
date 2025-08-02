package org.example.aktanoopproject.controller;

import org.example.aktanoopproject.config.RevokedTokenService;
import org.example.aktanoopproject.dto.LoginDTO;
import org.example.aktanoopproject.dto.UserDTO;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RevokedTokenService revokedTokenService;


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserDTO student) {
        String result = authService.register(student);
        Map<String, String> json = new HashMap<>();
        json.put("message", result);
        return ResponseEntity.ok(json);
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
