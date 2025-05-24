package org.example.aktanoopproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.aktanoopproject.dto.AnswerCreateDto;
import org.example.aktanoopproject.dto.TaskCreateDto;
import org.example.aktanoopproject.dto.TaskResponseDto;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.service.TaskService;
import org.example.aktanoopproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
            @RequestPart("answers") String answersJson) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<AnswerCreateDto> answers = mapper.readValue(answersJson, new TypeReference<List<AnswerCreateDto>>() {});

        TaskCreateDto dto = new TaskCreateDto();
        dto.setQuestion(question);
        dto.setAnswers(answers);

        taskService.createTask(dto);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/next")
    public ResponseEntity<List<TaskResponseDto>> getNextTasks(
            Authentication authentication) {  // Возьми Authentication напрямую
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Object principal = authentication.getPrincipal();
        System.out.println("Principal class: " + principal.getClass());
        // Пробуй привести principal к нужному типу или получать email
        String email = authentication.getName();
        User currentUser = userService.getUserByEmail(email);
        return ResponseEntity.ok(taskService.getNewTasksForUser(currentUser, 20));
    }


}
