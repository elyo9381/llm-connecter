package com.kafibara.chatbot.ai.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafibara.chatbot.ai.AIService;
import com.kafibara.chatbot.ai.ChatMessage;
import com.kafibara.chatbot.dto.ChatRequest;
import com.kafibara.chatbot.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("gemini")
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "ai.gemini.enabled", havingValue = "true", matchIfMissing = false)
public class GeminiService implements AIService {

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.base-url:https://generativelanguage.googleapis.com}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    private static final List<String> SUPPORTED_MODELS = Arrays.asList(
            "gemini-2.5-pro",
            "gemini-1.5-pro",
            "gemini-1.5-flash",
            "gemini-1.0-pro",
            "gemini-pro"
    );

    private static final String DEFAULT_MODEL = "gemini-2.5-pro";

    @Override
    public Mono<ChatResponse> generateResponse(ChatRequest request, List<ChatMessage> conversationHistory) {
        log.info("Gemini AI 응답 생성 시작 - 모델: {}, 질문: {}", request.getModel(), request.getQuestion());

        String model = request.getModel() != null ? request.getModel() : DEFAULT_MODEL;
        
        if (!SUPPORTED_MODELS.contains(model)) {
            log.warn("지원하지 않는 Gemini 모델: {}. 기본 모델 사용: {}", model, DEFAULT_MODEL);
            model = DEFAULT_MODEL;
        }

        final String finalModel = model; // final 변수로 선언

        return createWebClient()
                .post()
                .uri("/v1beta/models/{model}:generateContent?key={apiKey}", finalModel, apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(createGeminiRequest(request, conversationHistory))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> parseGeminiResponse(response, request, finalModel))
                .doOnSuccess(response -> log.info("Gemini AI 응답 생성 완료 - 응답 길이: {}", response.getAnswer().length()))
                .doOnError(error -> log.error("Gemini AI 응답 생성 실패", error))
                .onErrorReturn(createErrorResponse(request, finalModel));
    }

    @Override
    public Flux<String> generateStreamingResponse(ChatRequest request, List<ChatMessage> conversationHistory) {
        log.info("Gemini AI 스트리밍 응답 생성 시작 - 모델: {}", request.getModel());

        String model = request.getModel() != null ? request.getModel() : DEFAULT_MODEL;
        
        if (!SUPPORTED_MODELS.contains(model)) {
            model = DEFAULT_MODEL;
        }

        final String finalModel = model;

        // 일반 응답을 받아서 단어별로 스트리밍하는 방식으로 변경
        return generateResponse(request, conversationHistory)
                .flatMapMany(response -> {
                    String answer = response.getAnswer();
                    if (answer == null || answer.isEmpty()) {
                        return Flux.just("응답을 생성할 수 없습니다.");
                    }
                    
                    // 단어별로 나누어서 스트리밍 효과 생성
                    String[] words = answer.split(" ");
                    return Flux.fromArray(words)
                            .delayElements(Duration.ofMillis(50)) // 50ms 간격으로 단어 전송
                            .map(word -> word + " ");
                })
                .doOnComplete(() -> log.info("Gemini AI 스트리밍 응답 완료"))
                .doOnError(error -> log.error("Gemini AI 스트리밍 응답 실패", error))
                .onErrorReturn("죄송합니다. Gemini AI 서비스에 일시적인 문제가 발생했습니다.");
    }

    @Override
    public List<String> getSupportedModels() {
        return SUPPORTED_MODELS;
    }

    @Override
    public String getServiceName() {
        return "gemini";
    }

    @Override
    public String getDisplayName() {
        return "Google Gemini";
    }

    @Override
    public String getDefaultModel() {
        return DEFAULT_MODEL;
    }

    private WebClient createWebClient() {
        return webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    private Map<String, Object> createGeminiRequest(ChatRequest request, List<ChatMessage> conversationHistory) {
        Map<String, Object> requestBody = new HashMap<>();
        
        // Gemini API 형식에 맞게 contents 구성
        List<Map<String, Object>> contents = conversationHistory.stream()
                .map(this::convertToGeminiMessage)
                .collect(Collectors.toList());
        
        // 현재 사용자 질문 추가
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        Map<String, Object> userParts = new HashMap<>();
        userParts.put("text", request.getQuestion());
        userMessage.put("parts", List.of(userParts));
        contents.add(userMessage);
        
        requestBody.put("contents", contents);
        
        // 생성 설정
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("topK", 40);
        generationConfig.put("topP", 0.95);
        generationConfig.put("maxOutputTokens", 2048);
        requestBody.put("generationConfig", generationConfig);
        
        // 안전 설정
        List<Map<String, Object>> safetySettings = Arrays.asList(
                createSafetySetting("HARM_CATEGORY_HARASSMENT", "BLOCK_MEDIUM_AND_ABOVE"),
                createSafetySetting("HARM_CATEGORY_HATE_SPEECH", "BLOCK_MEDIUM_AND_ABOVE"),
                createSafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "BLOCK_MEDIUM_AND_ABOVE"),
                createSafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_MEDIUM_AND_ABOVE")
        );
        requestBody.put("safetySettings", safetySettings);
        
        return requestBody;
    }

    private Map<String, Object> convertToGeminiMessage(ChatMessage message) {
        Map<String, Object> geminiMessage = new HashMap<>();
        
        // ChatMessage의 role을 Gemini 형식으로 변환
        String role = "user".equals(message.role()) ? "user" : "model";
        geminiMessage.put("role", role);
        
        Map<String, Object> parts = new HashMap<>();
        parts.put("text", message.content());
        geminiMessage.put("parts", List.of(parts));
        
        return geminiMessage;
    }

    private Map<String, Object> createSafetySetting(String category, String threshold) {
        Map<String, Object> setting = new HashMap<>();
        setting.put("category", category);
        setting.put("threshold", threshold);
        return setting;
    }

    private ChatResponse parseGeminiResponse(String responseBody, ChatRequest request, String model) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            
            String answer = "죄송합니다. 응답을 생성할 수 없습니다.";
            
            if (root.has("candidates") && root.get("candidates").isArray() && root.get("candidates").size() > 0) {
                JsonNode candidate = root.get("candidates").get(0);
                if (candidate.has("content") && candidate.get("content").has("parts")) {
                    JsonNode parts = candidate.get("content").get("parts");
                    if (parts.isArray() && parts.size() > 0 && parts.get(0).has("text")) {
                        answer = parts.get(0).get("text").asText();
                    }
                }
            }

            return ChatResponse.builder()
                    .question(request.getQuestion())
                    .answer(answer)
                    .model(model)
                    .createdAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Gemini 응답 파싱 실패", e);
            return createErrorResponse(request, model);
        }
    }

    private String parseStreamingResponse(String chunk) {
        try {
            // 빈 줄이나 data: 접두사 제거
            String cleanChunk = chunk.trim();
            if (cleanChunk.isEmpty() || cleanChunk.startsWith("data: ")) {
                cleanChunk = cleanChunk.replace("data: ", "").trim();
            }
            
            if (cleanChunk.isEmpty()) {
                return "";
            }
            
            log.debug("파싱할 청크: {}", cleanChunk);
            
            JsonNode root = objectMapper.readTree(cleanChunk);
            
            // Gemini 스트리밍 응답 구조 확인
            if (root.has("candidates") && root.get("candidates").isArray() && root.get("candidates").size() > 0) {
                JsonNode candidate = root.get("candidates").get(0);
                if (candidate.has("content") && candidate.get("content").has("parts")) {
                    JsonNode parts = candidate.get("content").get("parts");
                    if (parts.isArray() && parts.size() > 0 && parts.get(0).has("text")) {
                        String text = parts.get(0).get("text").asText();
                        log.debug("파싱된 텍스트: {}", text);
                        return text;
                    }
                }
            }
            
            // 다른 형식도 시도
            if (root.has("text")) {
                return root.get("text").asText();
            }
            
            if (root.isTextual()) {
                return root.asText();
            }
            
            return "";
        } catch (Exception e) {
            log.debug("스트리밍 청크 파싱 실패 - 청크: {}, 오류: {}", chunk, e.getMessage());
            return "";
        }
    }

    private ChatResponse createErrorResponse(ChatRequest request, String model) {
        return ChatResponse.builder()
                .question(request.getQuestion())
                .answer("죄송합니다. Gemini AI 서비스에 일시적인 문제가 발생했습니다. 잠시 후 다시 시도해주세요.")
                .model(model)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
