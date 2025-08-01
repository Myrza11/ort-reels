package org.example.aktanoopproject.controller;

import org.example.aktanoopproject.model.FriendRequest;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.service.FriendService;
import org.example.aktanoopproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;

    @PostMapping("/invite/{friendId}")
    public void addFriend(@PathVariable Long friendId) {
        friendService.addFriend(friendId);
    }

    @GetMapping("/get-my-inviting")
    public List<FriendRequest> getMyInviting(Authentication currentUser) {
        return friendService.getMyInviting(userService.getUserByEmail(currentUser.getName()));
    }

    @GetMapping("/get-request")
    public List<FriendRequest> getRequest(Authentication currentUser) {
        return friendService.getRequests(userService.getUserByEmail(currentUser.getName()));
    }

    @DeleteMapping("/delete/{friendId}")
    public void deleteFriend(@PathVariable Long friendId, @AuthenticationPrincipal User currentUser) {
        friendService.deleteFriend(friendId, currentUser);
    }

    @PostMapping("/accept/{userId}")
    public void acceptInviting(@PathVariable Long userId) {
        friendService.acceptInviting(userId);
    }

}
