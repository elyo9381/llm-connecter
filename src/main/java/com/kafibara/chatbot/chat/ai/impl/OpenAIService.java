package com.kafibara.chatbot.ai.impl;

import com.kafibara.chatbot.chat.ai.AIService;
import com.kafibara.chatbot.chat.ai.ChatMessage;
import com.kafibara.chatbot.chat.dto.ChatRequest;
import com.kafibara.chatbot.chat.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("openai")
@ConditionalOnProperty(name = "ai.openai.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class OpenAIService implements AIService {
    
    private final WebClient webClient;
    
    @Value("${ai.openai.api-key}")
    private String apiKey;
    
    @Value("${ai.openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;
    
    private static final List<String> SUPPORTED_MODELS = List.of(
        "gpt-4", "gpt-4-turbo", "gpt-3.5-turbo", "gpt-4o", "gpt-4o-mini"
    );
    
    @Override
    public Mono<ChatResponse> generateResponse(ChatRequest request, List<ChatMessage> conversationHistory) {
        if (!isApiKeyConfigured()) {
            return Mono.error(new IllegalStateException("OpenAI API 키가 설정되지 않았습니다."));
        }
        
        var requestBody = buildRequestBody(request, conversationHistory, false);
        
        return webClient.post()
            .uri(baseUrl + "/chat/completions")
            .header("Authorization", "Bearer " + apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .map(this::extractResponse)
            .doOnError(error -> log.error("OpenAI API 호출 실패", error))
            .onErrorMap(this::handleApiError);
    }
    
    @Override
    public Flux<String> generateStreamingResponse(ChatRequest request, List<ChatMessage> conversationHistory) {
        if (!isApiKeyConfigured()) {
            return Flux.error(new IllegalStateException("OpenAI API 키가 설정되지 않았습니다."));
        }
        
        var requestBody = buildRequestBody(request, conversationHistory, true);
        
        return webClient.post()
            .uri(baseUrl + "/chat/completions")
            .header("Authorization", "Bearer " + apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToFlux(String.class)
            .map(this::extractStreamingContent)
            .filter(content -> !content.isEmpty())
            .doOnError(error -> log.error("OpenAI 스트리밍 API 호출 실패", error))
            .onErrorResume(error -> Flux.just("죄송합니다. 응답 생성 중 오류가 발생했습니다."));
    }
    
    @Override
    public List<String> getSupportedModels() {
        return SUPPORTED_MODELS;
    }
    
    @Override
    public String getServiceName() {
        return "openai";
    }

    @Override
    public String getDisplayName() {
        return "OpenAI";
    }
    
    @Override
    public String getDefaultModel() {
        return "gpt-3.5-turbo";
    }
    
    private boolean isApiKeyConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty() && !apiKey.startsWith("your-");
    }
    
    private Map<String, Object> buildRequestBody(ChatRequest request, List<ChatMessage> history, boolean stream) {
        var messages = history.stream()
            .map(msg -> Map.of("role", msg.role(), "content", msg.content()))
            .collect(Collectors.toList());
        
        messages.add(Map.of("role", "user", "content", request.getQuestion()));
        
        return Map.of(
            "model", request.getModel() != null ? request.getModel() : getDefaultModel(),
            "messages", messages,
            "stream", stream,
            "max_tokens", 1000,
            "temperature", 0.7
        );
    }
    
    private ChatResponse extractResponse(Map<String, Object> response) {
        try {
            var choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new RuntimeException("OpenAI 응답에서 choices를 찾을 수 없습니다.");
            }
            
            var message = (Map<String, Object>) choices.get(0).get("message");
            var content = (String) message.get("content");
            var model = (String) response.get("model");
            
            return ChatResponse.builder()
                .answer(content != null ? content : "응답을 생성할 수 없습니다.")
                .model(model)
                .build();
        } catch (Exception e) {
            log.error("OpenAI 응답 파싱 실패", e);
            return ChatResponse.builder()
                .answer("응답 처리 중 오류가 발생했습니다.")
                .model(getDefaultModel())
                .build();
        }
    }
    
    private String extractStreamingContent(String chunk) {
        // SSE 형식의 스트리밍 데이터에서 content 추출
        // 실제 구현에서는 더 정교한 JSON 파싱이 필요
        if (chunk.startsWith("data: ")) {
            String jsonData = chunk.substring(6).trim();
            if ("[DONE]".equals(jsonData)) {
                return "";
            }
            // 간단한 구현 - 실제로는 JSON 파싱 필요
            return jsonData;
        }
        return "";
    }
    
    private Throwable handleApiError(Throwable error) {
        log.error("OpenAI API 오류: {}", error.getMessage());
        return new RuntimeException("AI 서비스 호출 중 오류가 발생했습니다: " + error.getMessage());
    }
}
