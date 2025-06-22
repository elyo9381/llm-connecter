package com.kafibara.chatbot.repository;

import com.kafibara.chatbot.entity.Chat;
import com.kafibara.chatbot.entity.Thread;
import com.kafibara.chatbot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    
    List<Chat> findByThreadOrderByCreatedAtAsc(Thread thread);
    
    Page<Chat> findByThread_UserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    Page<Chat> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Chat c WHERE c.createdAt >= :startDate")
    long countByCreatedAtAfter(LocalDateTime startDate);
    
    // 보고서용 메서드들
    List<Chat> findByCreatedAtBetweenOrderByCreatedAtAsc(LocalDateTime startDate, LocalDateTime endDate);
}
