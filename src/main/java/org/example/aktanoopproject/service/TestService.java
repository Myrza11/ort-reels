package org.example.aktanoopproject.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Test;
import org.example.aktanoopproject.model.*;
import org.example.aktanoopproject.repository.QuestionRepository;
import org.example.aktanoopproject.repository.TestSessionRepository;
import org.example.aktanoopproject.repository.TestSetRepository;
import org.example.aktanoopproject.repository.UserAnswerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestSetRepository testSetRepo;
    private final QuestionRepository questionRepo;
    private final TestSessionRepository sessionRepo;
    private final UserAnswerRepository userAnswerRepo;

    public TestSet createTestSet(String title, int timeLimit, List<Long> questionIds) {
        TestSet test = new TestSet();
        test.setTitle(title);
        test.setTimeLimitMinutes(timeLimit);

        List<Question> questions = questionRepo.findAllById(questionIds);
        test.setQuestions(questions);

        return testSetRepo.save(test);
    }

    public TestSession startSession(Long testSetId, User user) {
        TestSet testSet = testSetRepo.findById(testSetId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        if (!testSet.isActive()) throw new RuntimeException("Test is inactive");

        TestSession session = new TestSession();
        session.setTestSet(testSet);
        session.setStartedAt(LocalDateTime.now());
        session.setSubmitted(false);
        session.setUser(user);

        return sessionRepo.save(session);
    }

    public void submitAnswer(Long sessionId, Long questionId, Long answerId, User user) {
        TestSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.isSubmitted() || isTimeExpired(session))
            throw new RuntimeException("Test session is closed");

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setSession(session);
        userAnswer.setQuestion(new Question() {{ setId(questionId); }});
        userAnswer.setSelectedAnswer(new Answer() {{ setId(answerId); }});

        userAnswerRepo.save(userAnswer);
    }

    public void finishSession(Long sessionId) {
        TestSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.isSubmitted()) {
            session.setSubmitted(true);
            session.setEndedAt(LocalDateTime.now());
            sessionRepo.save(session);
        }
    }

    private boolean isTimeExpired(TestSession session) {
        LocalDateTime deadline = session.getStartedAt().plusMinutes(session.getTestSet().getTimeLimitMinutes());
        return LocalDateTime.now().isAfter(deadline);
    }

    public List<TestSet> getTestSet() {
        return testSetRepo.findAll();
    }

    public List<TestSession> getUserSession(User user) {
        return sessionRepo.getAllByUser(user);
    }
}
