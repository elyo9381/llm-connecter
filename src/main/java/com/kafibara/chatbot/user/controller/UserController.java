package com.kafibara.chatbot.user.controller;

import com.kafibara.chatbot.user.dto.UserResponse;
import com.kafibara.chatbot.user.entity.User;
import com.kafibara.chatbot.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "사용자 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    
    private final UserRepository userRepository;
    
    @GetMapping
    @Operation(summary = "사용자 목록 조회", description = "모든 사용자 목록을 조회합니다. (관리자 전용)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserResponse> response = users.map(user -> UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "사용자 상세 조회", description = "특정 사용자의 상세 정보를 조회합니다. (관리자 전용)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
        
        return ResponseEntity.ok(response);
    }
}
