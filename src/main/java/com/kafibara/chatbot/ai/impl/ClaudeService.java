package com.kafibara.chatbot.ai.impl;

import com.kafibara.chatbot.ai.AIService;
import com.kafibara.chatbot.ai.ChatMessage;
import com.kafibara.chatbot.dto.ChatRequest;
import com.kafibara.chatbot.dto.ChatResponse;
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

@Service("claude")
@ConditionalOnProperty(name = "ai.claude.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class ClaudeService implements AIService {
    
    private final WebClient webClient;
    
    @Value("${ai.claude.api-key}")
    private String apiKey;
    
    @Value("${ai.claude.base-url:https://api.anthropic.com}")
    private String baseUrl;
    
    private static final List<String> SUPPORTED_MODELS = List.of(
        "claude-3-opus-20240229", "claude-3-sonnet-20240229", "claude-3-haiku-20240307"
    );
    
    @Override
    public Mono<ChatResponse> generateResponse(ChatRequest request, List<ChatMessage> conversationHistory) {
        if (!isApiKeyConfigured()) {
            return Mono.error(new IllegalStateException("Claude API 키가 설정되지 않았습니다."));
        }
        
        var requestBody = buildRequestBody(request, conversationHistory);
        
        return webClient.post()
            .uri(baseUrl + "/v1/messages")
            .header("Authorization", "Bearer " + apiKey)
            .header("anthropic-version", "2023-06-01")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .map(this::extractResponse)
            .doOnError(error -> log.error("Claude API 호출 실패", error))
            .onErrorMap(this::handleApiError);
    }
    
    @Override
    public Flux<String> generateStreamingResponse(ChatRequest request, List<ChatMessage> conversationHistory) {
        // Claude 스트리밍은 현재 간단한 구현으로 대체
        return generateResponse(request, conversationHistory)
            .flatMapMany(response -> {
                String[] words = response.getAnswer().split(" ");
                return Flux.fromArray(words)
                    .delayElements(java.time.Duration.ofMillis(100))
                    .map(word -> word + " ");
            });
    }
    
    @Override
    public List<String> getSupportedModels() {
        return SUPPORTED_MODELS;
    }
    
    @Override
    public String getServiceName() {
        return "claude";
    }

    @Override
    public String getDisplayName() {
        return "Anthropic Claude";
    }
    
    @Override
    public String getDefaultModel() {
        return "claude-3-haiku-20240307";
    }
    
    private boolean isApiKeyConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty() && !apiKey.startsWith("your-");
    }
    
    private Map<String, Object> buildRequestBody(ChatRequest request, List<ChatMessage> history) {
        var messages = history.stream()
            .map(msg -> Map.of("role", msg.role(), "content", msg.content()))
            .collect(Collectors.toList());
        
        messages.add(Map.of("role", "user", "content", request.getQuestion()));
        
        return Map.of(
            "model", request.getModel() != null ? request.getModel() : getDefaultModel(),
            "messages", messages,
            "max_tokens", 1000
        );
    }
    
    private ChatResponse extractResponse(Map<String, Object> response) {
        try {
            var content = (List<Map<String, Object>>) response.get("content");
            if (content == null || content.isEmpty()) {
                throw new RuntimeException("Claude 응답에서 content를 찾을 수 없습니다.");
            }
            
            var text = (String) content.get(0).get("text");
            var model = (String) response.get("model");
            
            return ChatResponse.builder()
                .answer(text != null ? text : "응답을 생성할 수 없습니다.")
                .model(model)
                .build();
        } catch (Exception e) {
            log.error("Claude 응답 파싱 실패", e);
            return ChatResponse.builder()
                .answer("응답 처리 중 오류가 발생했습니다.")
                .model(getDefaultModel())
                .build();
        }
    }
    
    private Throwable handleApiError(Throwable error) {
        log.error("Claude API 오류: {}", error.getMessage());
        return new RuntimeException("Claude AI 서비스 호출 중 오류가 발생했습니다: " + error.getMessage());
    }
}
