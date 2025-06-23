package com.kafibara.chatbot.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReportResponse {
    
    private long signupCount;
    
    private long loginCount;
    
    private long chatCount;
    
    private LocalDate reportDate;
}
