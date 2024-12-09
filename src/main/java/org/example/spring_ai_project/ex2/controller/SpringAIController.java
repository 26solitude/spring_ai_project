package org.example.spring_ai_project.ex2.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/prompt")
    public String prompt(@RequestBody ChatRequest request){
        return chatClient.prompt()
                .user(request.getMessage())
                .call()
                .content();
    }

    public static class ChatRequest{
        @JsonProperty("message")
        private String message;

        public String getMessage() {
            return message;
        }
    }
}
