package org.example.spring_ai_project.ex1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    private final WebClient webClient;

    public OpenAIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
    }

    public String getChatCompletion(String prompt) {
        try {
            return webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(Map.of(
                            "model", "gpt-3.5-turbo",
                            "messages", List.of(Map.of("role", "user", "content", prompt))
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            // 로깅 추가
            System.err.println("OpenAI API 호출 오류: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
