package com.kafibara.chatbot.ai.impl;

import com.kafibara.chatbot.ai.AIService;
import com.kafibara.chatbot.ai.ChatMessage;
import com.kafibara.chatbot.dto.ChatRequest;
import com.kafibara.chatbot.dto.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service("mock")
@ConditionalOnProperty(name = "ai.mock.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class MockAIService implements AIService {
    
    private final Random random = new Random();
    
    private static final List<String> MOCK_RESPONSES = List.of(
        "안녕하세요! 저는 AI 어시스턴트입니다. 어떻게 도와드릴까요?",
        "흥미로운 질문이네요! 더 자세히 설명해 주시겠어요?",
        "그에 대해 생각해보니, 여러 관점에서 접근할 수 있을 것 같습니다.",
        "좋은 지적입니다. 이런 방법은 어떨까요?",
        "제가 이해한 바로는... 맞나요?",
        "더 구체적인 예시를 들어주시면 더 정확한 답변을 드릴 수 있을 것 같습니다."
    );
    
    private static final List<String> SUPPORTED_MODELS = List.of(
        "mock-gpt-3.5", "mock-gpt-4", "mock-claude", "mock-gemini"
    );
    
    @Override
    public Mono<ChatResponse> generateResponse(ChatRequest request, List<ChatMessage> conversationHistory) {
        log.info("Mock AI 서비스 호출 - 질문: {}", request.getQuestion());
        
        // 실제 AI 서비스 호출을 시뮬레이션하기 위한 지연
        return Mono.delay(Duration.ofMillis(500 + random.nextInt(1000)))
            .map(delay -> {
                String response = generateMockResponse(request.getQuestion(), conversationHistory);
                return ChatResponse.builder()
                    .answer(response)
                    .model(request.getModel() != null ? request.getModel() : getDefaultModel())
                    .build();
            });
    }
    
    @Override
    public Flux<String> generateStreamingResponse(ChatRequest request, List<ChatMessage> conversationHistory) {
        log.info("Mock AI 스트리밍 서비스 호출 - 질문: {}", request.getQuestion());
        
        String fullResponse = generateMockResponse(request.getQuestion(), conversationHistory);
        String[] words = fullResponse.split(" ");
        
        return Flux.fromArray(words)
            .delayElements(Duration.ofMillis(100 + random.nextInt(200)))
            .map(word -> word + " ");
    }
    
    @Override
    public List<String> getSupportedModels() {
        return SUPPORTED_MODELS;
    }
    
    @Override
    public String getServiceName() {
        return "mock";
    }

    @Override
    public String getDisplayName() {
        return "Mock AI";
    }
    
    @Override
    public String getDefaultModel() {
        return "mock-gpt-3.5";
    }
    
    private String generateMockResponse(String question, List<ChatMessage> history) {
        // 질문에 따른 간단한 응답 생성 로직
        String lowerQuestion = question.toLowerCase();
        
        if (lowerQuestion.contains("안녕") || lowerQuestion.contains("hello")) {
            return "안녕하세요! 저는 Mock AI 어시스턴트입니다. 무엇을 도와드릴까요?";
        }
        
        if (lowerQuestion.contains("이름") || lowerQuestion.contains("name")) {
            return "저는 Mock AI 서비스입니다. 테스트 목적으로 만들어진 가상의 AI입니다.";
        }
        
        if (lowerQuestion.contains("날씨") || lowerQuestion.contains("weather")) {
            return "죄송하지만 실시간 날씨 정보는 제공할 수 없습니다. 날씨 앱이나 웹사이트를 확인해 주세요.";
        }
        
        if (lowerQuestion.contains("시간") || lowerQuestion.contains("time")) {
            return "현재 시간은 시스템 시계를 확인해 주세요. 저는 실시간 정보에 접근할 수 없습니다.";
        }
        
        // 대화 히스토리가 있으면 참고
        if (!history.isEmpty()) {
            return "이전 대화를 참고하여 답변드리면, " + getRandomResponse() + " 추가로 궁금한 점이 있으시면 언제든 물어보세요!";
        }
        
        return getRandomResponse();
    }
    
    private String getRandomResponse() {
        return MOCK_RESPONSES.get(random.nextInt(MOCK_RESPONSES.size()));
    }
}
