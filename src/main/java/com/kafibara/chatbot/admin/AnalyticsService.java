package com.kafibara.chatbot.admin;

import com.kafibara.chatbot.feedback.dto.FeedbackAnalyticsResponse;
import com.kafibara.chatbot.feedback.entity.Feedback;
import com.kafibara.chatbot.chat.repository.ChatRepository;
import com.kafibara.chatbot.feedback.repository.FeedbackRepository;
import com.kafibara.chatbot.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {
    
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final FeedbackRepository feedbackRepository;
    
    public ActivityReportResponse getDailyActivityReport() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        long signupCount = userRepository.countByCreatedAtAfter(startOfDay);
        long chatCount = chatRepository.countByCreatedAtAfter(startOfDay);
        
        // 로그인 수는 별도 테이블이 없으므로 0으로 설정 (추후 구현 가능)
        long loginCount = 0;
        
        log.info("일일 활동 보고서 생성 - 가입: {}, 로그인: {}, 대화: {}", signupCount, loginCount, chatCount);
        
        return ActivityReportResponse.builder()
                .signupCount(signupCount)
                .loginCount(loginCount)
                .chatCount(chatCount)
                .reportDate(startOfDay.toLocalDate())
                .build();
    }
    
    public FeedbackAnalyticsResponse getFeedbackAnalytics() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        // 전체 피드백 통계
        long totalFeedbacks = feedbackRepository.count();
        long positiveFeedbacks = feedbackRepository.countByIsPositive(true);
        long negativeFeedbacks = feedbackRepository.countByIsPositive(false);
        
        // 오늘 피드백 통계
        long todayTotalFeedbacks = feedbackRepository.countByCreatedAtAfter(startOfDay);
        long todayPositiveFeedbacks = feedbackRepository.countByIsPositiveAndCreatedAtAfter(true, startOfDay);
        long todayNegativeFeedbacks = feedbackRepository.countByIsPositiveAndCreatedAtAfter(false, startOfDay);
        
        // 상태별 피드백 통계
        long pendingFeedbacks = feedbackRepository.countByStatus(Feedback.Status.PENDING);
        long resolvedFeedbacks = feedbackRepository.countByStatus(Feedback.Status.RESOLVED);
        
        // 만족도 계산 (긍정 피드백 비율)
        double satisfactionRate = totalFeedbacks > 0 ? (double) positiveFeedbacks / totalFeedbacks * 100 : 0.0;
        double todaySatisfactionRate = todayTotalFeedbacks > 0 ? (double) todayPositiveFeedbacks / todayTotalFeedbacks * 100 : 0.0;
        
        log.info("피드백 분석 완료 - 전체: {}, 긍정: {}, 부정: {}, 만족도: {}%", 
            totalFeedbacks, positiveFeedbacks, negativeFeedbacks, String.format("%.1f", satisfactionRate));
        
        return FeedbackAnalyticsResponse.builder()
            .totalFeedbacks(totalFeedbacks)
            .positiveFeedbacks(positiveFeedbacks)
            .negativeFeedbacks(negativeFeedbacks)
            .todayTotalFeedbacks(todayTotalFeedbacks)
            .todayPositiveFeedbacks(todayPositiveFeedbacks)
            .todayNegativeFeedbacks(todayNegativeFeedbacks)
            .pendingFeedbacks(pendingFeedbacks)
            .resolvedFeedbacks(resolvedFeedbacks)
            .satisfactionRate(satisfactionRate)
            .todaySatisfactionRate(todaySatisfactionRate)
            .reportDate(startOfDay.toLocalDate())
            .build();
    }
}
