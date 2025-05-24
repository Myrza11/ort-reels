package org.example.aktanoopproject.repository;

import org.example.aktanoopproject.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable; // ✅
import java.util.List;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t NOT IN :usedTasks")
    List<Task> findNewTasksForUser(@Param("usedTasks") Set<Task> usedTasks, Pageable pageable);

}
