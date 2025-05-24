package org.example.aktanoopproject.service;

import jakarta.transaction.Transactional;
import org.example.aktanoopproject.dto.UserUpdateDTO;
import org.example.aktanoopproject.model.*;
import org.example.aktanoopproject.repository.FriendRequestRepository;
import org.example.aktanoopproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public void update(UserUpdateDTO userUpdateDTO, User currentUser) {
        if (userUpdateDTO.getName() != null) currentUser.setName(userUpdateDTO.getName());
        if (userUpdateDTO.getPassword() != null) currentUser.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        if (userUpdateDTO.getSurname() != null) currentUser.setSurname(userUpdateDTO.getSurname());
        if (userUpdateDTO.getInterests() != null) currentUser.setInterest(userUpdateDTO.getInterests());
        userRepository.save(currentUser);
    }

    public List<User> filter(Set<Interest> interests) {
        return userRepository.filterByInterest(interests);
    }
    @Transactional
    public void updateAvatar(String email, MultipartFile avatarFile) throws IOException {
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (currentUser == null) {
            throw new RuntimeException("Пользователь не найден или не аутентифицирован.");
        }
        byte[] avatarBytes = avatarFile.getBytes();
        currentUser.setAvatar(avatarBytes);
        userRepository.save(currentUser);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getFriendsOfUser(User user) {
        List<FriendRequest> acceptedRequests = friendRequestRepository
                .findBySenderOrRecipientAndStatus(user, user, RequestStatus.ACCEPTED);

        Set<User> friends = new HashSet<>();
        for (FriendRequest request : acceptedRequests) {
                friends.add(request.getRecipient());
                friends.add(request.getSender());
        }

        return new ArrayList<>(friends);
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameLikeIgnoreCase(name);
    }

}
