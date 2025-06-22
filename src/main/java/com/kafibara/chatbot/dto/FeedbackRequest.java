package com.kafibara.chatbot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {
    
    @NotNull(message = "대화 ID는 필수입니다.")
    private Long chatId;
    
    @NotNull(message = "피드백 유형은 필수입니다.")
    private Boolean isPositive;
}
