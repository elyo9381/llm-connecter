package com.kafibara.chatbot.dto;

import com.kafibara.chatbot.entity.Feedback;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackStatusUpdateRequest {
    
    @NotNull(message = "상태는 필수입니다.")
    private Feedback.Status status;
}
