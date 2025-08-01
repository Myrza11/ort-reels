package org.example.aktanoopproject.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.aktanoopproject.model.FriendRequest;
import org.example.aktanoopproject.model.RequestStatus;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.repository.FriendRequestRepository;
import org.example.aktanoopproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public List<FriendRequest> getMyInviting(User currentUser) {
        System.out.println("getMyInviting");
        System.out.println(currentUser.getUsername());
        System.out.println(friendRequestRepository.getAllBySender(currentUser));

        return friendRequestRepository.getAllBySender(currentUser);
    }
    public List<FriendRequest> getRequests(User currentUser) {
        System.out.println("getMyInviting");
        System.out.println(currentUser.getUsername());
        System.out.println(friendRequestRepository.getAllBySender(currentUser));
        return friendRequestRepository.getAllByRecipient(currentUser);
    }

    public void addFriend(Long friendId) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        // Загружаем текущего пользователя из базы по username
        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));        Optional<User> friendUser = userRepository.findById(friendId);
        if (!friendUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        FriendRequest friendRequest = new FriendRequest(currentUser, friendUser.get());
        friendRequestRepository.save(friendRequest);
    }

    public void deleteFriend(Long friendId, User currentUser) {
        Optional<FriendRequest> request = friendRequestRepository.findByUsersInEitherOrder(friendId, currentUser.getId());

        if (request.isPresent()) {
            friendRequestRepository.delete(request.get());
        } else {
            throw new EntityNotFoundException("Friend relationship not found.");
        }
    }

    @Transactional
    public void acceptInviting(Long friendId) {
        // Получаем имя пользователя из контекста безопасности
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        // Загружаем текущего пользователя из базы по username
        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Optional<User> friendUser = userRepository.findById(friendId);
        System.out.println("Friend accepted: " + friendUser.get().getName());

        System.out.println("Current user: " + currentUser.getName());
        List<FriendRequest> friendRequests = friendRequestRepository.getAllBySender(friendUser.get());
        List<FriendRequest> friendRequests2 = friendRequestRepository.getAllBySender(currentUser);

        System.out.println("friendRequest of Aktan" + friendRequests);
        System.out.println("friendRequests of Myrza" + friendRequests2);
        Optional<FriendRequest> request = friendRequestRepository
                .findByUsersInEitherOrder(friendId, currentUser.getId());


        if (request.isPresent()) {
            FriendRequest friendRequest = request.get();
            friendRequest.setStatus(RequestStatus.ACCEPTED);
            friendRequestRepository.save(friendRequest);
        } else {
            throw new EntityNotFoundException("Friend relationship not found.");
        }
    }


}
