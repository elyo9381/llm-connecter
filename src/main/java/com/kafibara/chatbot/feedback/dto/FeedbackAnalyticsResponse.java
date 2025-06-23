package com.kafibara.chatbot.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackAnalyticsResponse {
    
    // 전체 피드백 통계
    private long totalFeedbacks;
    private long positiveFeedbacks;
    private long negativeFeedbacks;
    
    // 오늘 피드백 통계
    private long todayTotalFeedbacks;
    private long todayPositiveFeedbacks;
    private long todayNegativeFeedbacks;
    
    // 상태별 피드백 통계
    private long pendingFeedbacks;
    private long resolvedFeedbacks;
    
    // 만족도 (%)
    private double satisfactionRate;
    private double todaySatisfactionRate;
    
    private LocalDate reportDate;
}
