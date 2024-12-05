package org.example.spring_ai_project.controller;

import org.example.spring_ai_project.service.OpenAIService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        return openAIService.getChatCompletion(prompt);
    }
}
