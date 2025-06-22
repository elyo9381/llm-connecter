package com.kafibara.chatbot.controller;

import com.kafibara.chatbot.dto.ActivityReportResponse;
import com.kafibara.chatbot.dto.FeedbackAnalyticsResponse;
import com.kafibara.chatbot.service.AnalyticsService;
import com.kafibara.chatbot.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 전용 API")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    
    private final AnalyticsService analyticsService;
    private final ReportService reportService;
    
    @GetMapping("/activity-report")
    @Operation(summary = "일일 활동 보고서", description = "오늘 하루 동안의 사용자 활동을 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityReportResponse> getDailyActivityReport() {
        ActivityReportResponse report = analyticsService.getDailyActivityReport();
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/feedback-analytics")
    @Operation(summary = "피드백 분석", description = "피드백 통계 및 만족도 분석 결과를 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FeedbackAnalyticsResponse> getFeedbackAnalytics() {
        FeedbackAnalyticsResponse analytics = analyticsService.getFeedbackAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/reports/daily-chat")
    @Operation(summary = "일일 대화 보고서 다운로드", description = "오늘 하루 동안의 모든 대화를 CSV 형태로 다운로드합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadDailyChatReport() throws IOException {
        byte[] csvData = reportService.generateDailyChatReport();
        
        String filename = "daily_chat_report_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv";
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
            .body(csvData);
    }
    
    @GetMapping("/reports/weekly-chat")
    @Operation(summary = "주간 대화 보고서 다운로드", description = "최근 7일간의 모든 대화를 CSV 형태로 다운로드합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadWeeklyChatReport() throws IOException {
        byte[] csvData = reportService.generateWeeklyChatReport();
        
        String filename = "weekly_chat_report_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv";
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
            .body(csvData);
    }
    
    @GetMapping("/reports/monthly-chat")
    @Operation(summary = "월간 대화 보고서 다운로드", description = "최근 30일간의 모든 대화를 CSV 형태로 다운로드합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadMonthlyChatReport() throws IOException {
        byte[] csvData = reportService.generateMonthlyChatReport();
        
        String filename = "monthly_chat_report_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv";
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
            .body(csvData);
    }
}
