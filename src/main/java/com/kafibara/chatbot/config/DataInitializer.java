package com.kafibara.chatbot.config;

import com.kafibara.chatbot.user.entity.User;
import com.kafibara.chatbot.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        try {
            initializeAdminUser();
        } catch (Exception e) {
            log.error("데이터 초기화 중 오류 발생: {}", e.getMessage(), e);
            // 오류가 발생해도 애플리케이션 시작을 중단하지 않음
        }
    }
    
    private void initializeAdminUser() {
        String adminEmail = "admin@chatbot.com";
        
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123!"))
                    .name("관리자")
                    .role(User.Role.ADMIN)
                    .build();
            
            userRepository.save(admin);
            log.info("관리자 계정이 생성되었습니다: {}", adminEmail);
            log.info("관리자 비밀번호: admin123!");
        } else {
            log.info("관리자 계정이 이미 존재합니다: {}", adminEmail);
        }
    }
}
