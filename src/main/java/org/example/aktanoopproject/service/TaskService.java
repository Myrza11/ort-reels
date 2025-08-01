package org.example.aktanoopproject.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.aktanoopproject.dto.*;
import org.example.aktanoopproject.model.*;
import org.example.aktanoopproject.repository.AnswerRepository;
import org.example.aktanoopproject.repository.TaskRepository;
import org.example.aktanoopproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private final String imgbbApiKey = "f0913dc1297d300ea171c5c2715270e3";

    public List<QuestionTheme> getTheme() {
        return List.of(QuestionTheme.values());
    }

    public void createTask(MultipartFile questionFile, String answersJson, QuestionTheme questionThemes) throws IOException {
        String base64Image = convertToBase64(questionFile);
        String imageUrl = uploadToImgbb(base64Image);

        Question question = new Question();
        question.setQuestion(imageUrl);
        question.setQuestionTheme(Set.of(questionThemes));

        ObjectMapper mapper = new ObjectMapper();
        List<Answer> answers = mapper.readValue(answersJson, new TypeReference<List<Answer>>() {});
        question.setAnswers(answers);

        taskRepository.save(question);

        System.out.println("Image URL: " + imageUrl);
        System.out.println("Answers JSON: " + answersJson);
    }

    private String convertToBase64(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    private String uploadToImgbb(String base64Image) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String body = "image=" + URLEncoder.encode(base64Image, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.imgbb.com/1/upload?key=" + imgbbApiKey))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Ошибка загрузки изображения: " + response.body());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            return jsonNode.get("data").get("url").asText();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки изображения: " + e.getMessage(), e);
        }
    }

    public List<QuestionResponseDto> getNewTasksForUser(User user, int limit, Set<QuestionTheme> questionThemes) {
        Set<Question> usedQuestions = user.getUsedQuestions();
        if (usedQuestions == null) {
            usedQuestions = Collections.emptySet();
        }

        List<Question> newQuestions;
        if (questionThemes != null) {
            newQuestions = taskRepository.findNewTasksForUser(usedQuestions, questionThemes, PageRequest.of(0, limit));
            System.out.println(newQuestions + "question themes: " + questionThemes);
            if (newQuestions.isEmpty()) {
                System.out.println(newQuestions + "question themes:2 " + questionThemes);
                Iterator<Question> iterator = user.getUsedQuestions().iterator();
                while (iterator.hasNext()) {
                    Question q = iterator.next();
                    if (!Collections.disjoint(q.getQuestionTheme(), questionThemes)) {
                        iterator.remove();
                    }
                }
                newQuestions = taskRepository.findNewTasksForUser(usedQuestions, questionThemes, PageRequest.of(0, limit));
            }
        } else {
            newQuestions = taskRepository.findNewTasksForUser(usedQuestions, PageRequest.of(0, limit));
            if (newQuestions.isEmpty()) {
                List<Question> allQuestions = taskRepository.findAll(PageRequest.of(0, limit)).getContent();
                user.getUsedQuestions().clear();
                newQuestions = allQuestions;
            }
        }
        System.out.println(newQuestions + "question themes: 3" + questionThemes);


        user.getUsedQuestions().addAll(newQuestions);
        userRepository.save(user);
        System.out.println(newQuestions + "question themes: 4" + questionThemes);


        List<QuestionResponseDto> dtoList = new ArrayList<>();
        for (Question question : newQuestions) {
            QuestionResponseDto dto = new QuestionResponseDto();
            dto.setId(question.getId());
            dto.setQuestion(question.getQuestion());

            List<AnswerResponseDto> answersDto = new ArrayList<>();
            for (Answer answer : question.getAnswers()) {
                AnswerResponseDto answerDto = new AnswerResponseDto(answer.getId(), answer.getAnswer());
                answersDto.add(answerDto);
            }
            dto.setAnswers(answersDto);

            dtoList.add(dto);
        }

        return dtoList;
    }


    public boolean checkAnswer(Long id) {
        Optional<Answer> answer = answerRepository.getAnswerById(id);
        if (!answer.isPresent()) {
            throw new RuntimeException("There is no such answer");
        }
        return answer.get().isCorrect();
    }

    // ✅ Новый метод для чата
    public Optional<Question> findById(Long id) {
        return taskRepository.findById(id);
    }
}
