package org.example.aktanoopproject.service;

import jakarta.mail.Session;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Test;
import org.example.aktanoopproject.model.*;
import org.example.aktanoopproject.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestSetRepository testSetRepo;
    private final QuestionRepository questionRepo;
    private final TestSessionRepository sessionRepo;
    private final UserAnswerRepository userAnswerRepo;
    private final AnswerRepository answerRepo;

    public TestSet createTestSet(String title, int timeLimit, List<Long> questionIds) {
        TestSet test = new TestSet();
        test.setTitle(title);
        test.setTimeLimitMinutes(timeLimit);
        System.out.println("timeLimit: " + timeLimit);
        System.out.println("test time limit: " + test.getTimeLimitMinutes());

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
        sessionRepo.save(session);
        return session;
    }

    public void submitAnswer(Long sessionId, Long questionId, Long answerId) {
        TestSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.isSubmitted() || isTimeExpired(session)) {
            System.out.println("Session already submitted" + session.isSubmitted() + "the time expired" + isTimeExpired(session));
            session.setSubmitted(false);
            session.setEndedAt(LocalDateTime.now());
            sessionRepo.save(session);
            throw new RuntimeException("Test session is closed");
        }
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setSession(session);
        Question question = questionRepo.getById(questionId);
        userAnswer.setQuestion(question);
        Answer answer = answerRepo.getAnswerById(answerId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        userAnswer.setSelectedAnswer(answer);
        System.out.println(userAnswer);
        System.out.println(answer.toString());
        System.out.println("answer is" + answer.getId() + "answer isCorrect" + answer.isCorrect());
        userAnswerRepo.save(userAnswer);
        session.getUserAnswers().add(userAnswer);
        sessionRepo.save(session);

    }

    public String finishSession(Long sessionId, User user) {
        List<TestSession> sessions = sessionRepo.getAllByUser(user);
        TestSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        System.out.println("session: " + sessions.contains(session));
        System.out.println("session: " + session.isSubmitted());
        if (session.isSubmitted() && sessions.contains(session)) {
            return "Session already submitted" + session.isSubmitted() + "the time expired";
        }
        if (sessions.contains(session)) {
            session.setSubmitted(true);
            session.setEndedAt(LocalDateTime.now());
            sessionRepo.save(session);
        }
        int correct = 0;
        int incorrect = 0;
        System.out.println("session: " + session.getUserAnswers());
        for (UserAnswer userAnswer : session.getUserAnswers()){
            System.out.println("userAnswer: " + userAnswer.getSelectedAnswer());
            System.out.println("userAnswer: " + userAnswer.getSelectedAnswer().isCorrect());

            if (userAnswer.getSelectedAnswer().isCorrect()){
                correct++;
            } else {
                incorrect++;
            }
        }
        return "The correct answers" + correct + "/" + incorrect + " is: \n" + session.getUserAnswers().toString();
    }

    private boolean isTimeExpired(TestSession session) {
        LocalDateTime deadline = session.getStartedAt().plusMinutes(session.getTestSet().getTimeLimitMinutes());
        System.out.println("deadline " + deadline);
        return LocalDateTime.now().isAfter(deadline);
    }

    public List<TestSet> getTestSet() {
        return testSetRepo.findAll();
    }

    public List<TestSession> getUserSession(User user) {
        return sessionRepo.getAllByUser(user);
    }
}
