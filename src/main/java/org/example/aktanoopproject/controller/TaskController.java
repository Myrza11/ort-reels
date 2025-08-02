package org.example.aktanoopproject.controller;

import org.example.aktanoopproject.dto.QuestionResponseDto;
import org.example.aktanoopproject.model.Interest;
import org.example.aktanoopproject.model.QuestionTheme;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.service.TaskService;
import org.example.aktanoopproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createTask(
            @RequestPart("question") MultipartFile question,
            @RequestPart("answers") String answersJson,
            @RequestPart("questionTheme") String themesRaw) throws IOException {

        Set<QuestionTheme> questionThemesSet = Arrays.stream(themesRaw.split(","))
                .map(String::trim)
                .map(QuestionTheme::valueOf)
                .collect(Collectors.toSet());

        taskService.createTask(question, answersJson, questionThemesSet);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-theme")
    public ResponseEntity<List<QuestionTheme>> getTheme() {
        return ResponseEntity.ok(taskService.getTheme());
    }



    @GetMapping("/next")
    public ResponseEntity<List<QuestionResponseDto>> getNextTasks(
            Authentication authentication,
            @RequestParam(value = "questionTheme", required = false) Set<QuestionTheme> questionThemes) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = authentication.getName();
        User currentUser = userService.getUserByEmail(email);

        if (questionThemes == null || questionThemes.isEmpty()) {
            questionThemes = EnumSet.allOf(QuestionTheme.class);
        }

        return ResponseEntity.ok(taskService.getNewTasksForUser(currentUser, 2, questionThemes));
    }


    @PostMapping
    public boolean checkAnswer(@RequestParam Long id) {
        return taskService.checkAnswer(id);
    }
}
