package com.kafibara.chatbot.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    
    @Bean
    public Counter chatRequestCounter(MeterRegistry meterRegistry) {
        return Counter.builder("chatbot.chat.requests")
            .description("Total number of chat requests")
            .register(meterRegistry);
    }
    
    @Bean
    public Counter feedbackCounter(MeterRegistry meterRegistry) {
        return Counter.builder("chatbot.feedback.total")
            .description("Total number of feedbacks")
            .register(meterRegistry);
    }
    
    @Bean
    public Counter userRegistrationCounter(MeterRegistry meterRegistry) {
        return Counter.builder("chatbot.user.registrations")
            .description("Total number of user registrations")
            .register(meterRegistry);
    }
    
    @Bean
    public Timer aiResponseTimer(MeterRegistry meterRegistry) {
        return Timer.builder("chatbot.ai.response.time")
            .description("AI response time")
            .register(meterRegistry);
    }
}
