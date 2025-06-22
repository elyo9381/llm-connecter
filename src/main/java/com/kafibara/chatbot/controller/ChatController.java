package com.kafibara.chatbot.controller;

import com.kafibara.chatbot.dto.ChatRequest;
import com.kafibara.chatbot.dto.ChatResponse;
import com.kafibara.chatbot.entity.User;
import com.kafibara.chatbot.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat", description = "대화 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class ChatController {
    
    private final ChatService chatService;
    
    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "AI 채팅", description = "스트리밍 방식으로 AI와 대화합니다.")
    public Flux<String> chat(
            @Valid @RequestBody ChatRequest request,
            @AuthenticationPrincipal User user) {
        
        log.info("스트리밍 채팅 요청 - 사용자: {}, 서비스: {}, 슽질문: {}",
                user.getEmail(), request.getAiService(), request.getQuestion());
        
        return chatService.createStreamingChat(request, user)
            .doOnComplete(() -> log.info("스트리밍 채팅 완료 - 사용자: {}", user.getEmail()))
            .onErrorResume(error -> {
                log.error("스트리밍 채팅 오류 - 사용자: {}, 오류: {}", user.getEmail(), error.getMessage());
                return Flux.just("data: 죄송합니다. 응답 생성 중 오류가 발생했습니다.\n\n");
            });
    }
    
    @GetMapping
    @Operation(summary = "대화 목록 조회", description = "사용자의 모든 대화를 조회합니다.")
    public ResponseEntity<Page<ChatResponse>> getChats(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        
        Page<ChatResponse> chats = chatService.getUserChats(user, pageable);
        return ResponseEntity.ok(chats);
    }
    
    @GetMapping("/threads/{threadId}")
    @Operation(summary = "스레드별 대화 조회", description = "특정 스레드의 모든 대화를 조회합니다.")
    public ResponseEntity<List<ChatResponse>> getThreadChats(
            @PathVariable Long threadId,
            @AuthenticationPrincipal User user) {
        
        List<ChatResponse> chats = chatService.getThreadChats(threadId, user);
        return ResponseEntity.ok(chats);
    }
    
    @DeleteMapping("/threads/{threadId}")
    @Operation(summary = "스레드 삭제", description = "특정 스레드와 관련된 모든 대화를 삭제합니다.")
    public ResponseEntity<Void> deleteThread(
            @PathVariable Long threadId,
            @AuthenticationPrincipal User user) {
        
        chatService.deleteThread(threadId, user);
        return ResponseEntity.noContent().build();
    }
}
