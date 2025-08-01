package org.example.aktanoopproject.repository;

import org.example.aktanoopproject.model.TestSession;
import org.example.aktanoopproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestSessionRepository extends JpaRepository<TestSession, Long> {
    List<TestSession> getAllByUser(User user);
}
