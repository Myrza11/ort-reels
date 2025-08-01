package org.example.aktanoopproject.repository;

import org.example.aktanoopproject.model.Message;
import org.example.aktanoopproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiverOrderBySentAt(User sender, User receiver);
    List<Message> findByReceiverAndSenderOrderBySentAt(User receiver, User sender);
}
