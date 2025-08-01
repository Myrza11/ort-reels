package org.example.aktanoopproject.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class TestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private boolean submitted;

    @ManyToOne
    private TestSet testSet;

    @ManyToOne(fetch = FetchType.LAZY) // добавляем связь с пользователем
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<UserAnswer> userAnswers;
}
