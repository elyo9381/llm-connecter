package com.kafibara.chatbot.feedback.controller;

import com.kafibara.chatbot.feedback.dto.FeedbackRequest;
import com.kafibara.chatbot.feedback.dto.FeedbackResponse;
import com.kafibara.chatbot.feedback.dto.FeedbackStatusUpdateRequest;
import com.kafibara.chatbot.user.entity.User;
import com.kafibara.chatbot.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "피드백 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class FeedbackController {
    
    private final FeedbackService feedbackService;
    
    @PostMapping
    @Operation(summary = "피드백 생성", description = "특정 대화에 대한 피드백을 생성합니다.")
    public ResponseEntity<FeedbackResponse> createFeedback(
            @Valid @RequestBody FeedbackRequest request,
            @AuthenticationPrincipal User user) {
        
        FeedbackResponse response = feedbackService.createFeedback(request, user);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "피드백 목록 조회", description = "사용자의 피드백 목록을 조회합니다. 관리자는 모든 피드백을 조회할 수 있습니다.")
    public ResponseEntity<Page<FeedbackResponse>> getFeedbacks(
            @AuthenticationPrincipal User user,
            @Parameter(description = "긍정/부정 필터 (true: 긍정, false: 부정, null: 전체)")
            @RequestParam(required = false) Boolean isPositive,
            Pageable pageable) {
        
        Page<FeedbackResponse> feedbacks = feedbackService.getUserFeedbacks(user, pageable, isPositive);
        return ResponseEntity.ok(feedbacks);
    }
    
    @GetMapping("/{feedbackId}")
    @Operation(summary = "피드백 상세 조회", description = "특정 피드백의 상세 정보를 조회합니다.")
    public ResponseEntity<FeedbackResponse> getFeedback(
            @PathVariable Long feedbackId,
            @AuthenticationPrincipal User user) {
        
        FeedbackResponse feedback = feedbackService.getFeedback(feedbackId, user);
        return ResponseEntity.ok(feedback);
    }
    
    @PutMapping("/{feedbackId}/status")
    @Operation(summary = "피드백 상태 변경", description = "피드백의 상태를 변경합니다. (관리자 전용)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FeedbackResponse> updateFeedbackStatus(
            @PathVariable Long feedbackId,
            @Valid @RequestBody FeedbackStatusUpdateRequest request,
            @AuthenticationPrincipal User user) {
        
        FeedbackResponse response = feedbackService.updateFeedbackStatus(feedbackId, request.getStatus(), user);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{feedbackId}")
    @Operation(summary = "피드백 삭제", description = "피드백을 삭제합니다.")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable Long feedbackId,
            @AuthenticationPrincipal User user) {
        
        feedbackService.deleteFeedback(feedbackId, user);
        return ResponseEntity.noContent().build();
    }
}
