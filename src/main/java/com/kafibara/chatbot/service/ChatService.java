package com.kafibara.chatbot.service;

import com.kafibara.chatbot.ai.AIServiceFactory;
import com.kafibara.chatbot.ai.ChatMessage;
import com.kafibara.chatbot.dto.ChatRequest;
import com.kafibara.chatbot.dto.ChatResponse;
import com.kafibara.chatbot.entity.Chat;
import com.kafibara.chatbot.entity.Thread;
import com.kafibara.chatbot.entity.User;
import com.kafibara.chatbot.repository.ChatRepository;
import com.kafibara.chatbot.repository.ThreadRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    
    private final ChatRepository chatRepository;
    private final ThreadRepository threadRepository;
    private final AIServiceFactory aiServiceFactory;
    private final Counter chatRequestCounter;
    private final Timer aiResponseTimer;
    
    private static final int THREAD_TIMEOUT_MINUTES = 30;
    
    @Transactional
    public Mono<ChatResponse> createChat(ChatRequest request, User user) {
        chatRequestCounter.increment();
        
        return Mono.fromCallable(() -> {
            try {
                var thread = getOrCreateThread(user);
                var conversationHistory = buildConversationHistory(thread);
                var aiService = aiServiceFactory.getService(request.getAiService());
                
                log.info("사용자 {}가 {} 서비스로 질문: {}", 
                    user.getEmail(), aiService.getServiceName(), request.getQuestion());
                
                Timer.Sample sample = Timer.start();
                
                // Mono를 동기적으로 처리
                ChatResponse response = aiService.generateResponse(request, conversationHistory)
                    .doOnNext(res -> sample.stop(aiResponseTimer))
                    .block(); // 동기적으로 대기
                
                if (response != null) {
                    return saveChat(thread, request, response);
                } else {
                    throw new RuntimeException("AI 응답 생성 실패");
                }
            } catch (Exception e) {
                log.error("대화 생성 실패 - 사용자: {}, 오류: {}", user.getEmail(), e.getMessage(), e);
                throw e;
            }
        });
    }
    
    @Transactional
    public Flux<String> createStreamingChat(ChatRequest request, User user) {
        chatRequestCounter.increment();
        
        try {
            var thread = getOrCreateThread(user);
            var conversationHistory = buildConversationHistory(thread);
            var aiService = aiServiceFactory.getService(request.getAiService());
            
            log.info("사용자 {}가 {} 서비스로 스트리밍 질문: {}", 
                user.getEmail(), aiService.getServiceName(), request.getQuestion());
            
            // 동기적으로 응답 생성 및 저장
            ChatResponse response = aiService.generateResponse(request, conversationHistory).block();
            
            if (response != null) {
                // 응답을 즉시 저장
                ChatResponse savedResponse = saveChat(thread, request, response);
                log.info("응답 저장 완료 - Chat ID: {}, 길이: {} 문자", 
                    savedResponse.getChatId(), response.getAnswer().length());
                
                // 저장된 응답을 스트리밍으로 전송
                String answer = response.getAnswer();
                if (answer != null && !answer.isEmpty()) {
                    String[] words = answer.split(" ");
                    return Flux.fromArray(words)
                            .delayElements(Duration.ofMillis(50)) // 50ms 간격으로 단어 전송
                            .map(word -> word + " ");
                } else {
                    return Flux.just("응답을 생성할 수 없습니다.");
                }
            } else {
                return Flux.just("AI 서비스에서 응답을 생성하지 못했습니다.");
            }
            
        } catch (Exception e) {
            log.error("스트리밍 대화 생성 실패 - 사용자: {}, 오류: {}", user.getEmail(), e.getMessage(), e);
            return Flux.just("오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    private Mono<String> generateFollowUpQuestion(String previousAnswer, User user, Object aiService) {
        return Mono.fromCallable(() -> {
            // 이전 답변을 분석해서 추가 질문 생성
            String followUpPrompt = String.format(
                "다음 답변을 읽고, 더 깊이 있는 후속 질문 1개를 생성해주세요. 질문만 답변하세요:\n\n%s", 
                previousAnswer.length() > 1000 ? previousAnswer.substring(0, 1000) + "..." : previousAnswer
            );
            
            log.info("추가 질문 생성 중...");
            return followUpPrompt; // 실제로는 AI 서비스를 다시 호출해야 함
        });
    }
    
    public Page<ChatResponse> getUserChats(User user, Pageable pageable) {
        Page<Chat> chats;
        
        if (user.getRole() == User.Role.ADMIN) {
            chats = chatRepository.findAllByOrderByCreatedAtDesc(pageable);
        } else {
            chats = chatRepository.findByThread_UserOrderByCreatedAtDesc(user, pageable);
        }
        
        return chats.map(this::convertToChatResponse);
    }
    
    public List<ChatResponse> getThreadChats(Long threadId, User user) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new IllegalArgumentException("스레드를 찾을 수 없습니다."));
        
        // 권한 확인
        if (user.getRole() != User.Role.ADMIN && !thread.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 스레드에 접근할 권한이 없습니다.");
        }
        
        return chatRepository.findByThreadOrderByCreatedAtAsc(thread)
            .stream()
            .map(this::convertToChatResponse)
            .toList();
    }
    
    @Transactional
    public void deleteThread(Long threadId, User user) {
        Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new IllegalArgumentException("스레드를 찾을 수 없습니다."));
        
        if (user.getRole() != User.Role.ADMIN && !thread.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("자신의 스레드만 삭제할 수 있습니다.");
        }
        
        threadRepository.delete(thread);
        log.info("스레드 삭제 - ID: {}, 사용자: {}", threadId, user.getEmail());
    }
    
    private Thread getOrCreateThread(User user) {
        // User 엔티티의 threads 컬렉션에 접근하지 않고 직접 Repository로 조회
        var latestThread = threadRepository.findTopByUserIdOrderByUpdatedAtDesc(user.getId());
        
        if (latestThread.isEmpty() || isThreadExpired(latestThread.get())) {
            Thread newThread = Thread.builder()
                .user(user)
                .build();
            Thread savedThread = threadRepository.save(newThread);
            log.info("새 스레드 생성 - ID: {}, 사용자: {}", savedThread.getId(), user.getEmail());
            return savedThread;
        }
        
        return latestThread.get();
    }
    
    private boolean isThreadExpired(Thread thread) {
        return thread.getUpdatedAt()
            .isBefore(LocalDateTime.now().minusMinutes(THREAD_TIMEOUT_MINUTES));
    }
    
    private List<ChatMessage> buildConversationHistory(Thread thread) {
        return chatRepository.findByThreadOrderByCreatedAtAsc(thread)
            .stream()
            .flatMap(chat -> List.of(
                ChatMessage.user(chat.getQuestion()),
                ChatMessage.assistant(chat.getAnswer())
            ).stream())
            .toList();
    }
    
    private ChatResponse saveChat(Thread thread, ChatRequest request, ChatResponse response) {
        Chat chat = Chat.builder()
            .thread(thread)
            .question(request.getQuestion())
            .answer(response.getAnswer())
            .model(response.getModel())
            .build();
        
        Chat savedChat = chatRepository.save(chat);
        
        // 스레드 업데이트 시간 갱신
        thread.setUpdatedAt(LocalDateTime.now());
        threadRepository.save(thread);
        
        log.info("대화 저장 완료 - Chat ID: {}, Thread ID: {}", savedChat.getId(), thread.getId());
        
        return response.toBuilder()
            .chatId(savedChat.getId())
            .threadId(thread.getId())
            .question(request.getQuestion())
            .createdAt(savedChat.getCreatedAt())
            .build();
    }
    
    private ChatResponse convertToChatResponse(Chat chat) {
        return ChatResponse.builder()
            .chatId(chat.getId())
            .threadId(chat.getThread().getId())
            .question(chat.getQuestion())
            .answer(chat.getAnswer())
            .model(chat.getModel())
            .createdAt(chat.getCreatedAt())
            .build();
    }
}
