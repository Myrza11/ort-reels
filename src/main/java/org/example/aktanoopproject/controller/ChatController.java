package org.example.aktanoopproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.aktanoopproject.dto.MassageDTO;
import org.example.aktanoopproject.model.Message;
import org.example.aktanoopproject.model.Question;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.aktanoopproject.service.TaskService;
import org.example.aktanoopproject.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final UserService userService;
    private final TaskService taskService;


    /**
     * Отправка сообщения или задания
     */
    @PostMapping("/send/{receiverId}")
    public ResponseEntity<Message> sendMessage(@PathVariable Long receiverId,
            Authentication currentUser,
            @RequestBody MassageDTO massageDTO) {

        // Загружаем пользователей
        User sender = userService.getUserByEmail(currentUser.getName());
        User receiver = userService.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Загружаем задание, если передано
        Question question = massageDTO.getTaskId() != null
                ? taskService.findById(massageDTO.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"))
                : null;

        Message message = messageService.sendMessage(sender, receiver, massageDTO.getContent(), question);
        return ResponseEntity.ok(message);
    }

    /**
     * Получить все сообщения между двумя пользователями
     */
    @GetMapping("/{user2Id}")
    public ResponseEntity<List<Message>> getChat(Authentication currentUser, @PathVariable Long user2Id) {
        User user1 = userService.getUserByEmail(currentUser.getName());
        User user2 = userService.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User2 not found"));

        List<Message> chat = messageService.getChat(user1, user2);
        return ResponseEntity.ok(chat);
    }
}
