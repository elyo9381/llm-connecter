package com.kafibara.chatbot.controller;

import com.kafibara.chatbot.ai.AIServiceFactory;
import com.kafibara.chatbot.dto.AIServiceInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai-services")
@RequiredArgsConstructor
@Tag(name = "AI Services", description = "AI 서비스 정보 API")
@SecurityRequirement(name = "bearerAuth")
public class AIServiceController {
    
    private final AIServiceFactory aiServiceFactory;
    
    @GetMapping
    @Operation(summary = "사용 가능한 AI 서비스 목록", description = "현재 사용 가능한 모든 AI 서비스와 모델 정보를 조회합니다.")
    public ResponseEntity<List<AIServiceInfo>> getAvailableServices() {
        List<AIServiceInfo> services = aiServiceFactory.getAllServices().entrySet().stream()
            .map(entry -> AIServiceInfo.builder()
                .serviceName(entry.getKey())
                .displayName(entry.getValue().getDisplayName())
                .defaultModel(entry.getValue().getDefaultModel())
                .supportedModels(entry.getValue().getSupportedModels())
                .build())
            .toList();
        
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/names")
    @Operation(summary = "AI 서비스 이름 목록", description = "사용 가능한 AI 서비스 이름만 조회합니다.")
    public ResponseEntity<Map<String, Object>> getServiceNames() {
        var serviceNames = aiServiceFactory.getAvailableServiceNames();
        var defaultService = aiServiceFactory.getDefaultService();
        
        return ResponseEntity.ok(Map.of(
            "services", serviceNames,
            "defaultService", defaultService.getServiceName().toLowerCase()
        ));
    }
}
