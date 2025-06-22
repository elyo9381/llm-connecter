package com.kafibara.chatbot.ai;

import com.kafibara.chatbot.dto.ChatRequest;
import com.kafibara.chatbot.dto.ChatResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * AI 서비스 통합을 위한 인터페이스
 * 다양한 AI 서비스 (OpenAI, Claude, Gemini 등)를 지원
 */
public interface AIService {
    
    /**
     * 일반 채팅 응답 생성
     */
    Mono<ChatResponse> generateResponse(ChatRequest request, List<ChatMessage> conversationHistory);
    
    /**
     * 스트리밍 채팅 응답 생성
     */
    Flux<String> generateStreamingResponse(ChatRequest request, List<ChatMessage> conversationHistory);
    
    /**
     * 지원하는 모델 목록 반환
     */
    List<String> getSupportedModels();
    
    /**
     * AI 서비스 이름 반환
     */
    String getServiceName();
    
    /**
     * AI 서비스 표시 이름 반환
     */
    default String getDisplayName() {
        return getServiceName();
    }
    
    /**
     * 기본 모델명 반환
     */
    String getDefaultModel();
}
