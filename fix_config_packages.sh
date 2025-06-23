#!/bin/bash

# Config 패키지 세분화 후 패키지 선언 및 import 문 수정 스크립트

BASE_DIR="/Users/yowon/study/custom-chatbot/custom-chatbot/src/main/java/com/kafibara/chatbot"

echo "🔧 Config 패키지 세분화 후 import 문 수정 시작..."

# 1. Admin 패키지 파일들의 패키지 선언 수정
echo "👨‍💼 Admin 패키지 수정..."
find "$BASE_DIR/config/admin" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.config;/package com.kafibara.chatbot.config.admin;/g' {} \;

# 2. Exception 패키지 파일들의 패키지 선언 수정
echo "⚠️ Exception 패키지 수정..."
find "$BASE_DIR/config/exception" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.config;/package com.kafibara.chatbot.config.exception;/g' {} \;

# 3. Util 패키지 파일들의 패키지 선언 수정
echo "🔧 Util 패키지 수정..."
find "$BASE_DIR/config/util" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.config;/package com.kafibara.chatbot.config.util;/g' {} \;

# 4. 전체 프로젝트에서 admin 관련 import 수정
echo "🔗 Admin 관련 import 수정..."
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.AdminController;/import com.kafibara.chatbot.config.admin.AdminController;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.AnalyticsService;/import com.kafibara.chatbot.config.admin.AnalyticsService;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.ReportService;/import com.kafibara.chatbot.config.admin.ReportService;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.HealthController;/import com.kafibara.chatbot.config.admin.HealthController;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.ActivityReportResponse;/import com.kafibara.chatbot.config.admin.ActivityReportResponse;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.ReportResponse;/import com.kafibara.chatbot.config.admin.ReportResponse;/g' {} \;

# 5. Exception 관련 import 수정
echo "⚠️ Exception 관련 import 수정..."
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.GlobalExceptionHandler;/import com.kafibara.chatbot.config.exception.GlobalExceptionHandler;/g' {} \;

# 6. JWT/Util 관련 import 수정
echo "🔐 JWT/Util 관련 import 수정..."
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.JwtAuthenticationFilter;/import com.kafibara.chatbot.config.util.JwtAuthenticationFilter;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.JwtUtil;/import com.kafibara.chatbot.config.util.JwtUtil;/g' {} \;

# 7. Admin 패키지 내부에서 서로 참조하는 경우 수정
echo "🔄 Admin 패키지 내부 참조 수정..."
find "$BASE_DIR/config/admin" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.admin\./import com.kafibara.chatbot.config.admin./g' {} \;

# 8. SecurityConfig에서 JwtAuthenticationFilter import 수정
echo "🔒 SecurityConfig import 수정..."
find "$BASE_DIR/config" -name "SecurityConfig.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.JwtAuthenticationFilter;/import com.kafibara.chatbot.config.util.JwtAuthenticationFilter;/g' {} \;

# 9. UserService에서 JwtUtil import 수정
echo "👤 UserService import 수정..."
find "$BASE_DIR/user" -name "UserService.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.JwtUtil;/import com.kafibara.chatbot.config.util.JwtUtil;/g' {} \;

# 10. JwtAuthenticationFilter에서 JwtUtil import 수정 (같은 패키지이므로 제거)
echo "🔧 같은 패키지 내 import 정리..."
find "$BASE_DIR/config/util" -name "JwtAuthenticationFilter.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.util\.JwtUtil;//g' {} \;

echo "✅ Config 패키지 세분화 및 import 수정 완료!"
