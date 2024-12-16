package org.example.spring_ai_project.ex3.repository;

import org.example.spring_ai_project.ex3.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByUserIdOrderByTimestamp(String userId);
}
