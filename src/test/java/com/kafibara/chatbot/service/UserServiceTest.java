package com.kafibara.chatbot.service;

import com.kafibara.chatbot.dto.LoginRequest;
import com.kafibara.chatbot.dto.SignupRequest;
import com.kafibara.chatbot.entity.User;
import com.kafibara.chatbot.repository.UserRepository;
import com.kafibara.chatbot.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private UserService userService;
    
    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    private User user;
    
    @BeforeEach
    void setUp() {
        signupRequest = SignupRequest.builder()
            .email("test@example.com")
            .password("password123")
            .name("테스트 사용자")
            .build();
        
        loginRequest = LoginRequest.builder()
            .email("test@example.com")
            .password("password123")
            .build();
        
        user = User.builder()
            .id(1L)
            .email("test@example.com")
            .password("encodedPassword")
            .name("테스트 사용자")
            .role(User.Role.MEMBER)
            .build();
    }
    
    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // Given
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail(), user.getRole().name())).thenReturn("jwt-token");
        
        // When
        var response = userService.signup(signupRequest);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(signupRequest.getEmail());
        assertThat(response.getName()).isEqualTo(signupRequest.getName());
        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getRole()).isEqualTo("MEMBER");
        
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(user.getEmail(), user.getRole().name());
    }
    
    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signup_Fail_EmailExists() {
        // Given
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> userService.signup(signupRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 존재하는 이메일입니다.");
        
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // Given
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail(), user.getRole().name())).thenReturn("jwt-token");
        
        // When
        var response = userService.login(loginRequest);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getName()).isEqualTo(user.getName());
        assertThat(response.getToken()).isEqualTo("jwt-token");
        
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        verify(jwtUtil).generateToken(user.getEmail(), user.getRole().name());
    }
    
    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void login_Fail_UserNotFound() {
        // Given
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> userService.login(loginRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 사용자입니다.");
        
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, never()).matches(any(), any());
    }
    
    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_Fail_WrongPassword() {
        // Given
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);
        
        // When & Then
        assertThatThrownBy(() -> userService.login(loginRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비밀번호가 일치하지 않습니다.");
        
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        verify(jwtUtil, never()).generateToken(any(), any());
    }
}
