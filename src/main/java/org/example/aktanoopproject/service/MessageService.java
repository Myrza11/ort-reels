package org.example.aktanoopproject.service;

import lombok.RequiredArgsConstructor;
import org.example.aktanoopproject.model.Message;
import org.example.aktanoopproject.model.RequestStatus;
import org.example.aktanoopproject.model.Question;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.repository.FriendRequestRepository;
import org.example.aktanoopproject.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final FriendRequestRepository friendRequestRepository;

    public Message sendMessage(User sender, User receiver, String content, Question question) {
        // 1. Проверка дружбы
        boolean areFriends = friendRequestRepository
                .existsBySenderAndRecipientAndStatus(sender, receiver, RequestStatus.ACCEPTED)
                || friendRequestRepository
                .existsBySenderAndRecipientAndStatus(receiver, sender, RequestStatus.ACCEPTED);

        if (!areFriends) {
            throw new RuntimeException("Вы не друзья! Сначала примите запрос.");
        }

        // 2. Сохраняем сообщение
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setQuestion(question);
        return messageRepository.save(message);
    }

    public List<Message> getChat(User user1, User user2) {
        List<Message> messages = new ArrayList<>();
        messages.addAll(messageRepository.findBySenderAndReceiverOrderBySentAt(user1, user2));
        messages.addAll(messageRepository.findByReceiverAndSenderOrderBySentAt(user1, user2));
        messages.sort(Comparator.comparing(Message::getSentAt));
        return messages;
    }
}
