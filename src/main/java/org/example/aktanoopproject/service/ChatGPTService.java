package org.example.aktanoopproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.aktanoopproject.model.Answer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGPTService {

    private final String OPENAI_API_KEY = ""; // TODO: Сохрани в .env или application.properties

    public String askWithImageAndOptions(String userQuestion, String imageUrl, String answers) {
        try {
            HttpClient client = HttpClient.newHttpClient();


            List<Map<String, Object>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", "Ты — помощник, который анализирует изображение и варианты ответов."));

            messages.add(Map.of("role", "user", "content", List.of(
                    Map.of("type", "image_url", "image_url",
                            Map.of("url", imageUrl)),
                    Map.of("type", "text", "text",
                            "вопрос пользователя " + userQuestion + "\n\n" +
                                    "это варианты отваеты задания на картинке" + answers)

            )));

            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("model", "gpt-4o"); // GPT-4o с Vision
            jsonMap.put("messages", messages);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(jsonMap);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = objectMapper.readTree(response.body());
            String gptText = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            return gptText;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка запроса к ChatGPT", e);
        }
    }


    private String answersToString(List<Answer> answers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < answers.size(); i++) {
            sb.append((i + 1)).append(". ").append(answers.get(i).getAnswer()).append("\n");
        }
        return sb.toString();
    }

}
