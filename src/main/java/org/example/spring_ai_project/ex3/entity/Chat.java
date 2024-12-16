package org.example.spring_ai_project.ex3.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document
@Getter
@Setter
public class Chat {
    @Id
    private String id;
    private String userId;
    private String role;
    private String content;
    private LocalDateTime timestamp;
}
