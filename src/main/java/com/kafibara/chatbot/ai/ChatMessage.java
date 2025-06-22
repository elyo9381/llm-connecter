package com.kafibara.chatbot.ai;

/**
 * 대화 메시지 DTO
 */
public record ChatMessage(String role, String content) {
    
    public static ChatMessage user(String content) {
        return new ChatMessage("user", content);
    }
    
    public static ChatMessage assistant(String content) {
        return new ChatMessage("assistant", content);
    }
    
    public static ChatMessage system(String content) {
        return new ChatMessage("system", content);
    }
}
