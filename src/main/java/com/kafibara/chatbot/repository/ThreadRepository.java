package com.kafibara.chatbot.repository;

import com.kafibara.chatbot.entity.Thread;
import com.kafibara.chatbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    
    Optional<Thread> findTopByUserOrderByUpdatedAtDesc(User user);
    
    Optional<Thread> findTopByUserIdOrderByUpdatedAtDesc(Long userId);
}
