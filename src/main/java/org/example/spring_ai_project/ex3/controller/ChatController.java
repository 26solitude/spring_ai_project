package org.example.spring_ai_project.ex3.controller;

import org.example.spring_ai_project.ex3.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final ChatClient chatClient;
    private final ChatService chatService;

    public ChatController(ChatClient chatClient, ChatService chatService) {
        this.chatClient = chatClient;
        this.chatService = chatService;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamResponse(@RequestParam String userId, @RequestParam String prompt) {
        chatService.addChat(userId, "user", prompt);

        var chatHistory = chatService.getChat(userId);

        var promptBuilder = chatClient.prompt();

        for (var chat : chatHistory) {
            if ("user".equals(chat.getRole())) {
                promptBuilder.messages(new UserMessage(chat.getContent()));
            } else if ("assistant".equals(chat.getRole())) {
                promptBuilder.messages(new AssistantMessage(chat.getContent()));
            }
        }

        return promptBuilder
                .user(u -> u.text(prompt))
                .stream()
                .content()
                .doOnNext(token -> chatService.addChat(userId, "assistant", token));
    }
}
