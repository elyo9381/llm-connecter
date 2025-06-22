package com.kafibara.chatbot.service;

import com.kafibara.chatbot.dto.FeedbackRequest;
import com.kafibara.chatbot.entity.Chat;
import com.kafibara.chatbot.entity.Feedback;
import com.kafibara.chatbot.entity.Thread;
import com.kafibara.chatbot.entity.User;
import com.kafibara.chatbot.repository.ChatRepository;
import com.kafibara.chatbot.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeedbackService 테스트")
class FeedbackServiceTest {
    
    @Mock
    private FeedbackRepository feedbackRepository;
    
    @Mock
    private ChatRepository chatRepository;
    
    @InjectMocks
    private FeedbackService feedbackService;
    
    private User user;
    private User otherUser;
    private User admin;
    private Thread thread;
    private Chat chat;
    private FeedbackRequest feedbackRequest;
    private Feedback feedback;
    
    @BeforeEach
    void setUp() {
        user = User.builder()
            .id(1L)
            .email("test@example.com")
            .name("테스트 사용자")
            .role(User.Role.MEMBER)
            .build();
        
        otherUser = User.builder()
            .id(2L)
            .email("other@example.com")
            .name("다른 사용자")
            .role(User.Role.MEMBER)
            .build();
        
        admin = User.builder()
            .id(3L)
            .email("admin@example.com")
            .name("관리자")
            .role(User.Role.ADMIN)
            .build();
        
        thread = Thread.builder()
            .id(1L)
            .user(user)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        
        chat = Chat.builder()
            .id(1L)
            .thread(thread)
            .question("테스트 질문")
            .answer("테스트 답변")
            .model("mock-gpt-3.5")
            .createdAt(OffsetDateTime.now())
            .build();
        
        feedbackRequest = FeedbackRequest.builder()
            .chatId(1L)
            .isPositive(true)
            .build();
        
        feedback = Feedback.builder()
            .id(1L)
            .user(user)
            .chat(chat)
            .isPositive(true)
            .status(Feedback.Status.PENDING)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
    }
    
    @Test
    @DisplayName("피드백 생성 성공 - 자신의 대화")
    void createFeedback_Success_OwnChat() {
        // Given
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
        when(feedbackRepository.existsByUserAndChat(user, chat)).thenReturn(false);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);
        
        // When
        var response = feedbackService.createFeedback(feedbackRequest, user);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getChatId()).isEqualTo(1L);
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getIsPositive()).isTrue();
        assertThat(response.getStatus()).isEqualTo("PENDING");
        
        verify(chatRepository).findById(1L);
        verify(feedbackRepository).existsByUserAndChat(user, chat);
        verify(feedbackRepository).save(any(Feedback.class));
    }
    
    @Test
    @DisplayName("피드백 생성 성공 - 관리자")
    void createFeedback_Success_Admin() {
        // Given
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
        when(feedbackRepository.existsByUserAndChat(admin, chat)).thenReturn(false);
        
        Feedback adminFeedback = Feedback.builder()
            .id(2L)
            .user(admin)
            .chat(chat)
            .isPositive(false)
            .status(Feedback.Status.PENDING)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(adminFeedback);
        
        FeedbackRequest adminRequest = FeedbackRequest.builder()
            .chatId(1L)
            .isPositive(false)
            .build();
        
        // When
        var response = feedbackService.createFeedback(adminRequest, admin);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(admin.getId());
        assertThat(response.getIsPositive()).isFalse();
        
        verify(feedbackRepository).save(any(Feedback.class));
    }
    
    @Test
    @DisplayName("피드백 생성 실패 - 존재하지 않는 대화")
    void createFeedback_Fail_ChatNotFound() {
        // Given
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> feedbackService.createFeedback(feedbackRequest, user))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 대화입니다.");
        
        verify(chatRepository).findById(1L);
        verify(feedbackRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("피드백 생성 실패 - 권한 없음")
    void createFeedback_Fail_NoPermission() {
        // Given
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
        
        // When & Then
        assertThatThrownBy(() -> feedbackService.createFeedback(feedbackRequest, otherUser))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 대화에 피드백을 남길 권한이 없습니다.");
        
        verify(chatRepository).findById(1L);
        verify(feedbackRepository, never()).existsByUserAndChat(any(), any());
        verify(feedbackRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("피드백 생성 실패 - 중복 피드백")
    void createFeedback_Fail_DuplicateFeedback() {
        // Given
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
        when(feedbackRepository.existsByUserAndChat(user, chat)).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> feedbackService.createFeedback(feedbackRequest, user))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 해당 대화에 피드백을 남겼습니다.");
        
        verify(feedbackRepository).existsByUserAndChat(user, chat);
        verify(feedbackRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("피드백 상태 변경 성공 - 관리자")
    void updateFeedbackStatus_Success_Admin() {
        // Given
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        
        Feedback updatedFeedback = Feedback.builder()
            .id(1L)
            .user(user)
            .chat(chat)
            .isPositive(true)
            .status(Feedback.Status.RESOLVED)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        
        when(feedbackRepository.save(feedback)).thenReturn(updatedFeedback);
        
        // When
        var response = feedbackService.updateFeedbackStatus(1L, Feedback.Status.RESOLVED, admin);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("RESOLVED");
        
        verify(feedbackRepository).findById(1L);
        verify(feedbackRepository).save(feedback);
    }
    
    @Test
    @DisplayName("피드백 상태 변경 실패 - 권한 없음")
    void updateFeedbackStatus_Fail_NoPermission() {
        // When & Then
        assertThatThrownBy(() -> feedbackService.updateFeedbackStatus(1L, Feedback.Status.RESOLVED, user))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("피드백 상태 변경 권한이 없습니다.");
        
        verify(feedbackRepository, never()).findById(any());
        verify(feedbackRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("피드백 삭제 성공 - 자신의 피드백")
    void deleteFeedback_Success_OwnFeedback() {
        // Given
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        
        // When & Then
        assertThatCode(() -> feedbackService.deleteFeedback(1L, user))
            .doesNotThrowAnyException();
        
        verify(feedbackRepository).findById(1L);
        verify(feedbackRepository).delete(feedback);
    }
    
    @Test
    @DisplayName("피드백 삭제 성공 - 관리자")
    void deleteFeedback_Success_Admin() {
        // Given
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        
        // When & Then
        assertThatCode(() -> feedbackService.deleteFeedback(1L, admin))
            .doesNotThrowAnyException();
        
        verify(feedbackRepository).delete(feedback);
    }
    
    @Test
    @DisplayName("피드백 삭제 실패 - 권한 없음")
    void deleteFeedback_Fail_NoPermission() {
        // Given
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        
        // When & Then
        assertThatThrownBy(() -> feedbackService.deleteFeedback(1L, otherUser))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 피드백을 삭제할 권한이 없습니다.");
        
        verify(feedbackRepository).findById(1L);
        verify(feedbackRepository, never()).delete(any());
    }
}
