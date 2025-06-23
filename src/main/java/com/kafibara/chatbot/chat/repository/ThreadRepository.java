package com.kafibara.chatbot.chat.repository;

import com.kafibara.chatbot.chat.entity.Thread;
import com.kafibara.chatbot.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    
    Optional<Thread> findTopByUserOrderByUpdatedAtDesc(User user);
    
    Optional<Thread> findTopByUserIdOrderByUpdatedAtDesc(Long userId);
}
