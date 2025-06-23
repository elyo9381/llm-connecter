package com.kafibara.chatbot.chat.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AIServiceFactory {
    
    private final Map<String, AIService> aiServices;
    
    /**
     * 서비스 이름으로 AI 서비스 반환
     */
    public AIService getService(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            return getDefaultService();
        }
        
        AIService service = aiServices.get(serviceName.toLowerCase());
        if (service == null) {
            log.warn("지원하지 않는 AI 서비스: {}. 기본 서비스를 사용합니다.", serviceName);
            return getDefaultService();
        }
        return service;
    }
    
    /**
     * 기본 AI 서비스 반환 (우선순위: OpenAI > Gemini > Mock)
     */
    public AIService getDefaultService() {
        // OpenAI 서비스 우선
        AIService openaiService = aiServices.get("openai");
        if (openaiService != null) {
            return openaiService;
        }
        
        // Gemini 서비스 차선책
        AIService geminiService = aiServices.get("gemini");
        if (geminiService != null) {
            return geminiService;
        }
        
        // Mock 서비스 최후 수단
        AIService mockService = aiServices.get("mock");
        if (mockService != null) {
            return mockService;
        }
        
        throw new IllegalStateException("사용 가능한 AI 서비스를 찾을 수 없습니다.");
    }
    
    /**
     * 사용 가능한 모든 AI 서비스 목록 반환
     */
    public Map<String, AIService> getAllServices() {
        return aiServices;
    }
    
    /**
     * 사용 가능한 AI 서비스 이름 목록 반환
     */
    public java.util.Set<String> getAvailableServiceNames() {
        return aiServices.keySet();
    }
}
