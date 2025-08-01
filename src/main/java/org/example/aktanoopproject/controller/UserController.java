package org.example.aktanoopproject.controller;

import org.example.aktanoopproject.dto.UserUpdateDTO;
import org.example.aktanoopproject.model.Interest;
import org.example.aktanoopproject.model.Question;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("/update")
    public void update(@RequestBody UserUpdateDTO updateStudentDTO, Authentication currentUser) {
        userService.update(updateStudentDTO, userService.getUserByEmail(currentUser.getName()));
    }

    @PatchMapping(value = "/avatar-update", consumes = "multipart/form-data")
    public void updateAvatar(@RequestParam("avatar") MultipartFile avatar, Authentication currentUser) throws IOException {
        userService.updateAvatar(userService.getUserByEmail(currentUser.getName()), avatar);
    }

    @GetMapping("/filter")
    public List<User> filterStudents(@RequestParam Set<Interest> interests) {
        return userService.filter(interests);
    }
    @GetMapping("/me")
    public User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getAll-user")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/friends")
    public List<User> getFriends(Authentication currentUser) {
        return userService.getFriendsOfUser(userService.getUserByEmail(currentUser.getName()));
    }

    @GetMapping("/search")
    public List<User> searchUsersByName(@RequestParam String query) {
        return userService.searchUsersByName(query);
    }

    @PostMapping("/save-question/{questionId}")
    public ResponseEntity<Void> saveQuestion(Authentication currentUser, @PathVariable Long questionId) {
        userService.saveQuestionForUser(userService.getUserByEmail(currentUser.getName()), questionId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/saved-questions")
    public ResponseEntity<Set<Question>> getSavedQuestions(Authentication currentUser) {
        String email = currentUser.getName();
        return ResponseEntity.ok(userService.getUserByEmail(email).getSavedQuestions());
    }
    @DeleteMapping("/delete-from-saved/{questionId}")
    public ResponseEntity<Set<Question>>  deleteSavedQuestion(Authentication currentUser, @PathVariable Long questionId) {
        String email = currentUser.getName();
        userService.deleteSavedQuestion(userService.getUserByEmail(email), questionId);
        return ResponseEntity.ok(userService.getUserByEmail(email).getSavedQuestions());
    }
}
