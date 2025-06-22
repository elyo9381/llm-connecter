package com.kafibara.chatbot.service;

import com.kafibara.chatbot.dto.FeedbackRequest;
import com.kafibara.chatbot.dto.FeedbackResponse;
import com.kafibara.chatbot.entity.Chat;
import com.kafibara.chatbot.entity.Feedback;
import com.kafibara.chatbot.entity.User;
import com.kafibara.chatbot.repository.ChatRepository;
import com.kafibara.chatbot.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {
    
    private final FeedbackRepository feedbackRepository;
    private final ChatRepository chatRepository;
    
    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request, User user) {
        // 대화 존재 확인
        Chat chat = chatRepository.findById(request.getChatId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대화입니다."));
        
        // 권한 확인 - 자신의 대화이거나 관리자인 경우만 피드백 가능
        if (user.getRole() != User.Role.ADMIN && 
            !chat.getThread().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 대화에 피드백을 남길 권한이 없습니다.");
        }
        
        // 중복 피드백 확인
        if (feedbackRepository.existsByUserAndChat(user, chat)) {
            throw new IllegalArgumentException("이미 해당 대화에 피드백을 남겼습니다.");
        }
        
        // 피드백 생성
        Feedback feedback = Feedback.builder()
            .user(user)
            .chat(chat)
            .isPositive(request.getIsPositive())
            .status(Feedback.Status.PENDING)
            .build();
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        
        log.info("피드백 생성 - 사용자: {}, 대화 ID: {}, 긍정: {}", 
            user.getEmail(), request.getChatId(), request.getIsPositive());
        
        return convertToFeedbackResponse(savedFeedback);
    }
    
    public Page<FeedbackResponse> getUserFeedbacks(User user, Pageable pageable, Boolean isPositive) {
        Page<Feedback> feedbacks;
        
        if (user.getRole() == User.Role.ADMIN) {
            // 관리자는 모든 피드백 조회 가능
            if (isPositive != null) {
                feedbacks = feedbackRepository.findByIsPositiveOrderByCreatedAtDesc(isPositive, pageable);
            } else {
                feedbacks = feedbackRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        } else {
            // 일반 사용자는 자신의 피드백만 조회
            feedbacks = feedbackRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        }
        
        return feedbacks.map(this::convertToFeedbackResponse);
    }
    
    @Transactional
    public FeedbackResponse updateFeedbackStatus(Long feedbackId, Feedback.Status status, User user) {
        // 관리자만 피드백 상태 변경 가능
        if (user.getRole() != User.Role.ADMIN) {
            throw new IllegalArgumentException("피드백 상태 변경 권한이 없습니다.");
        }
        
        Feedback feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드백입니다."));
        
        feedback.setStatus(status);
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        
        log.info("피드백 상태 변경 - ID: {}, 상태: {}, 관리자: {}", 
            feedbackId, status, user.getEmail());
        
        return convertToFeedbackResponse(updatedFeedback);
    }
    
    public FeedbackResponse getFeedback(Long feedbackId, User user) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드백입니다."));
        
        // 권한 확인 - 자신의 피드백이거나 관리자인 경우만 조회 가능
        if (user.getRole() != User.Role.ADMIN && 
            !feedback.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 피드백을 조회할 권한이 없습니다.");
        }
        
        return convertToFeedbackResponse(feedback);
    }
    
    @Transactional
    public void deleteFeedback(Long feedbackId, User user) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드백입니다."));
        
        // 권한 확인 - 자신의 피드백이거나 관리자인 경우만 삭제 가능
        if (user.getRole() != User.Role.ADMIN && 
            !feedback.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 피드백을 삭제할 권한이 없습니다.");
        }
        
        feedbackRepository.delete(feedback);
        
        log.info("피드백 삭제 - ID: {}, 사용자: {}", feedbackId, user.getEmail());
    }
    
    private FeedbackResponse convertToFeedbackResponse(Feedback feedback) {
        return FeedbackResponse.builder()
            .id(feedback.getId())
            .chatId(feedback.getChat().getId())
            .userId(feedback.getUser().getId())
            .userName(feedback.getUser().getName())
            .isPositive(feedback.getIsPositive())
            .status(feedback.getStatus().name())
            .createdAt(feedback.getCreatedAt())
            .updatedAt(feedback.getUpdatedAt())
            .build();
    }
}
