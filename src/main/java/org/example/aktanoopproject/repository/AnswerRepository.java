package org.example.aktanoopproject.repository;

import org.example.aktanoopproject.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> getAnswerById(Long id);
}
