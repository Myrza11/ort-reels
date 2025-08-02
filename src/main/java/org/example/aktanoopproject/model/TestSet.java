package org.example.aktanoopproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class TestSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // Название подборки

    private int timeLimitMinutes; // Временной лимит в минутах

    private boolean active = true; // Можно ли запускать тест

    @ManyToMany
    private List<Question> questions; // Можно выбрать из базы
}
