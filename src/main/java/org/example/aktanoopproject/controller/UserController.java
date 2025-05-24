package org.example.aktanoopproject.controller;

import org.example.aktanoopproject.dto.UserUpdateDTO;
import org.example.aktanoopproject.model.Interest;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void update(@RequestBody UserUpdateDTO updateStudentDTO, @AuthenticationPrincipal User currentUser) {
        userService.update(updateStudentDTO, currentUser);
    }

    @PatchMapping(value = "/avatar-update", consumes = "multipart/form-data")
    public void updateAvatar(@RequestParam("avatar") MultipartFile avatar, @AuthenticationPrincipal User currentUser) throws IOException {
        userService.updateAvatar(currentUser.getEmail(), avatar);
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
    public List<User> getFriends(@AuthenticationPrincipal User currentUser) {
        return userService.getFriendsOfUser(currentUser);
    }

    @GetMapping("/search")
    public List<User> searchUsersByName(@RequestParam String query) {
        return userService.searchUsersByName(query);
    }
}
