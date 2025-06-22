package com.kafibara.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    
    @NotBlank(message = "질문은 필수입니다.")
    private String question;
    
    private String model;
    
    private String aiService;
    
    @Builder.Default
    private Boolean isStreaming = false;
}
