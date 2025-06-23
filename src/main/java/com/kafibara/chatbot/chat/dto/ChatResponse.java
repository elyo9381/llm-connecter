package com.kafibara.chatbot.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    
    private Long chatId;
    
    private Long threadId;
    
    private String question;
    
    private String answer;
    
    private String model;
    
    private LocalDateTime createdAt;
}
