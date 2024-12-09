package org.example.spring_ai_project.ex2.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAIController {

    private final ChatClient chatClient;

    public SpringAIController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/prompt")
    public String prompt() {
        return chatClient.prompt()
                .user("SpringAI를 사용해 정상적으로 ChatGPT에게서 응답이 오는지 확인")
                .call()
                .content();
    }
}
