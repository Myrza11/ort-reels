package org.example.aktanoopproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.aktanoopproject.dto.CreateTestRequestDTO;
import org.example.aktanoopproject.dto.SubmitAnswerRequestDTO;
import org.example.aktanoopproject.model.TestSession;
import org.example.aktanoopproject.model.TestSet;
import org.example.aktanoopproject.service.TestService;
import org.example.aktanoopproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tests")
public class TestController {

    private final TestService testService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<TestSet> createTest(@RequestBody CreateTestRequestDTO request) {
        return ResponseEntity.ok(
                testService.createTestSet(request.getTitle(), request.getTimeLimitMinutes(), request.getQuestionIds())
        );
    }

    @PostMapping("/{testSetId}/start")
    public ResponseEntity<TestSession> startSession(@PathVariable Long testSetId, Authentication currentUser) {
        return ResponseEntity.ok(testService.startSession(testSetId, userService.getUserByEmail(currentUser.getName())));
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitAnswer(@RequestBody SubmitAnswerRequestDTO request) {
        testService.submitAnswer(request.getSessionId(), request.getQuestionId(), request.getAnswerId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sessionId}/finish")
    public ResponseEntity<String> finish(@PathVariable Long sessionId, Authentication currentUser) {

        return ResponseEntity.ok(testService.finishSession(sessionId, userService.getUserByEmail(currentUser.getName())));
    }
}
