package com.kafibara.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIServiceInfo {
    
    private String serviceName;
    
    private String displayName;
    
    private String defaultModel;
    
    private List<String> supportedModels;
}
