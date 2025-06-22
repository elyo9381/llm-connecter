package com.kafibara.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    
    private Long reportId;
    private Long threadId;
    private String title;
    private String summary;
    private String originalQuestion;
    private String originalAnswer;
    private List<FollowUpQA> followUpQuestions;
    private String conclusion;
    private LocalDateTime createdAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowUpQA {
        private String question;
        private String answer;
    }
}
