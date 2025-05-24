package org.example.aktanoopproject.service;

import org.example.aktanoopproject.config.JwtUtil;
import org.example.aktanoopproject.dto.UserDTO;
import org.example.aktanoopproject.model.*;
import org.example.aktanoopproject.repository.UserRepository;
import org.example.aktanoopproject.repository.VerificationTokenRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, VerificationTokenRepository tokenRepository, EmailService emailService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    public void register(UserDTO studentDTO) {
        User student = new User();
        student.setPassword(new BCryptPasswordEncoder().encode(studentDTO.getPassword()));
        student.setEmailVerified(false);
        student.setName(studentDTO.getName());
        student.setSurname(studentDTO.getSurname());
        student.setEmail(studentDTO.getEmail());
        student.setInterest(student.getInterest());

        userRepository.save(student);
        VerificationToken token = new VerificationToken(student);
        tokenRepository.save(token);

        emailService.sendVerificationEmail(student.getEmail(), token.getToken());
    }

    public String authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new RuntimeException("user not found");
        }
        if (!user.get().isEmailVerified()){
            throw new RuntimeException("user is not verified");
        }
        if (!new BCryptPasswordEncoder().matches(password, user.get().getPassword())) {
            throw new RuntimeException("password not match");
        }
        return jwtUtil.generateToken(user.get());
    }
}
