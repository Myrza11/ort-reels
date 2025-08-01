package org.example.aktanoopproject.repository;

import org.example.aktanoopproject.model.Question;
import org.example.aktanoopproject.model.QuestionTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable; // ✅
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Question, Long> {
    @Query("SELECT t FROM Question t WHERE t NOT IN :usedTasks")
    List<Question> findNewTasksForUser(@Param("usedTasks") Set<Question> usedQuestions, Pageable pageable);

    Optional<Question> getTaskById(Long id);

    @Query("SELECT DISTINCT t FROM Question t JOIN t.questionTheme theme WHERE t NOT IN :usedTasks AND theme IN :themes")
    List<Question> findNewTasksForUser(
            @Param("usedTasks") Set<Question> usedQuestions,
            @Param("themes") Set<QuestionTheme> questionThemes,
            Pageable pageable
    );

}
