package com.kafibara.chatbot.admin;

import com.kafibara.chatbot.chat.entity.Chat;
import com.kafibara.chatbot.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    
    private final ChatRepository chatRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public byte[] generateDailyChatReport() throws IOException {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        List<Chat> chats = chatRepository.findByCreatedAtBetweenOrderByCreatedAtAsc(startOfDay, endOfDay);
        
        log.info("일일 대화 보고서 생성 - 대화 수: {}", chats.size());
        
        return generateCsvReport(chats);
    }
    
    public byte[] generateWeeklyChatReport() throws IOException {
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfWeek = LocalDateTime.now();
        
        List<Chat> chats = chatRepository.findByCreatedAtBetweenOrderByCreatedAtAsc(startOfWeek, endOfWeek);
        
        log.info("주간 대화 보고서 생성 - 대화 수: {}", chats.size());
        
        return generateCsvReport(chats);
    }
    
    public byte[] generateMonthlyChatReport() throws IOException {
        LocalDateTime startOfMonth = LocalDateTime.now().minusDays(30).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfMonth = LocalDateTime.now();
        
        List<Chat> chats = chatRepository.findByCreatedAtBetweenOrderByCreatedAtAsc(startOfMonth, endOfMonth);
        
        log.info("월간 대화 보고서 생성 - 대화 수: {}", chats.size());
        
        return generateCsvReport(chats);
    }
    
    private byte[] generateCsvReport(List<Chat> chats) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            // BOM 추가 (Excel에서 한글 깨짐 방지)
            writer.write('\ufeff');
            
            // CSV 헤더
            writer.write("대화ID,스레드ID,사용자ID,사용자이름,사용자이메일,질문,답변,AI모델,생성일시\n");
            
            // 데이터 행
            for (Chat chat : chats) {
                // LazyInitializationException 방지를 위해 안전하게 접근
                String threadId = "N/A";
                String userId = "N/A";
                String userName = "N/A";
                String userEmail = "N/A";
                
                try {
                    if (chat.getThread() != null) {
                        threadId = String.valueOf(chat.getThread().getId());
                        if (chat.getThread().getUser() != null) {
                            userId = String.valueOf(chat.getThread().getUser().getId());
                            userName = chat.getThread().getUser().getName();
                            userEmail = chat.getThread().getUser().getEmail();
                        }
                    }
                } catch (Exception e) {
                    log.debug("LazyInitializationException 발생, 기본값 사용: {}", e.getMessage());
                }
                
                writer.write(String.format("%d,%s,%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%s\n",
                    chat.getId(),
                    threadId,
                    userId,
                    escapeCsv(userName),
                    escapeCsv(userEmail),
                    escapeCsv(chat.getQuestion()),
                    escapeCsv(chat.getAnswer()),
                    escapeCsv(chat.getModel() != null ? chat.getModel() : ""),
                    chat.getCreatedAt().format(DATE_FORMATTER)
                ));
            }
            
            writer.flush();
        }
        
        return outputStream.toByteArray();
    }
    
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        // CSV에서 따옴표와 줄바꿈 문자 처리
        return value.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ");
    }
}
