package org.example.aktanoopproject.controller;

import org.example.aktanoopproject.model.Question;
import org.example.aktanoopproject.repository.TaskRepository;
import org.example.aktanoopproject.service.ChatGPTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
public class GPTChatController {

    private final TaskRepository taskRepository;
    private final ChatGPTService chatGPTService;

    public GPTChatController(TaskRepository taskRepository, ChatGPTService chatGPTService) {
        this.taskRepository = taskRepository;
        this.chatGPTService = chatGPTService;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestParam Long taskId, @RequestParam String userQuestion) {
        Optional<Question> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        Question question = taskOptional.get();
        String imageUrl = question.getQuestion();
        String answers = question.toString();
        System.out.println(question.toString());

        String gptResponse = chatGPTService.askWithImageAndOptions(userQuestion, imageUrl, answers);


        return ResponseEntity.ok(gptResponse);
    }

}
