package com.kafibara.chatbot.feedback.repository;

import com.kafibara.chatbot.feedback.entity.Feedback;
import com.kafibara.chatbot.user.entity.User;
import com.kafibara.chatbot.chat.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    Page<Feedback> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    Page<Feedback> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    Page<Feedback> findByIsPositiveOrderByCreatedAtDesc(Boolean isPositive, Pageable pageable);
    
    boolean existsByUserAndChat(User user, Chat chat);
    
    // 분석용 메서드들
    long countByIsPositive(Boolean isPositive);
    
    long countByStatus(Feedback.Status status);
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.createdAt >= :startDate")
    long countByCreatedAtAfter(LocalDateTime startDate);
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.isPositive = :isPositive AND f.createdAt >= :startDate")
    long countByIsPositiveAndCreatedAtAfter(Boolean isPositive, LocalDateTime startDate);
}
