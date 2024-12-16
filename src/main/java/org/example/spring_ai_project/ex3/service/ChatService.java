package org.example.spring_ai_project.ex3.service;

import org.example.spring_ai_project.ex3.entity.Chat;
import org.example.spring_ai_project.ex3.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository){
        this.chatRepository = chatRepository;
    }

    public List<Chat> getChat(String userId){
        return chatRepository.findByUserIdOrderByTimestamp(userId);
    }

    public void addChat(String userId, String role, String content){
        Chat chat = new Chat();
        chat.setUserId(userId);
        chat.setRole(role);
        chat.setContent(content);
        chat.setTimestamp(LocalDateTime.now());
        chatRepository.save(chat);
    }

}
