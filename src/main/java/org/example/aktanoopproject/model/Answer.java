package org.example.aktanoopproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String answer;

    private boolean isCorrect; // хранится в базе, но фронту не отдаём!

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}