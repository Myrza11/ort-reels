package org.example.aktanoopproject.controller;

import org.example.aktanoopproject.dto.TaskCreateDto;
import org.example.aktanoopproject.dto.TaskResponseDto;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.service.TaskService;
import org.example.aktanoopproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Void> createTask(@RequestBody TaskCreateDto dto) {
        taskService.createTask(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/next")
    public ResponseEntity<List<TaskResponseDto>> getNextTasks(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(taskService.getNewTasksForUser(currentUser, limit));
    }

}
