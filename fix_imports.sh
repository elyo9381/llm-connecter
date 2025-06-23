#!/bin/bash

# 패키지 재구성 후 import 문 정교하게 수정하는 스크립트

BASE_DIR="/Users/yowon/study/custom-chatbot/custom-chatbot/src/main/java/com/kafibara/chatbot"

echo "🔧 Import 문 정교하게 수정 시작..."

# 1. User 패키지 내부 파일들의 패키지 선언 수정
echo "👤 User 패키지 내부 수정..."
find "$BASE_DIR/user" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.controller;/package com.kafibara.chatbot.user.controller;/g' {} \;
find "$BASE_DIR/user" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.service;/package com.kafibara.chatbot.user.service;/g' {} \;
find "$BASE_DIR/user" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.entity;/package com.kafibara.chatbot.user.entity;/g' {} \;
find "$BASE_DIR/user" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.repository;/package com.kafibara.chatbot.user.repository;/g' {} \;
find "$BASE_DIR/user" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.dto;/package com.kafibara.chatbot.user.dto;/g' {} \;

# 2. Chat 패키지 내부 파일들의 패키지 선언 수정
echo "💬 Chat 패키지 내부 수정..."
find "$BASE_DIR/chat" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.controller;/package com.kafibara.chatbot.chat.controller;/g' {} \;
find "$BASE_DIR/chat" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.service;/package com.kafibara.chatbot.chat.service;/g' {} \;
find "$BASE_DIR/chat" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.entity;/package com.kafibara.chatbot.chat.entity;/g' {} \;
find "$BASE_DIR/chat" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.repository;/package com.kafibara.chatbot.chat.repository;/g' {} \;
find "$BASE_DIR/chat" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.dto;/package com.kafibara.chatbot.chat.dto;/g' {} \;
find "$BASE_DIR/chat" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.ai;/package com.kafibara.chatbot.chat.ai;/g' {} \;

# 3. Feedback 패키지 내부 파일들의 패키지 선언 수정
echo "📝 Feedback 패키지 내부 수정..."
find "$BASE_DIR/feedback" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.controller;/package com.kafibara.chatbot.feedback.controller;/g' {} \;
find "$BASE_DIR/feedback" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.service;/package com.kafibara.chatbot.feedback.service;/g' {} \;
find "$BASE_DIR/feedback" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.entity;/package com.kafibara.chatbot.feedback.entity;/g' {} \;
find "$BASE_DIR/feedback" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.repository;/package com.kafibara.chatbot.feedback.repository;/g' {} \;
find "$BASE_DIR/feedback" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.dto;/package com.kafibara.chatbot.feedback.dto;/g' {} \;

# 4. Config 패키지 내부 파일들의 패키지 선언 수정
echo "⚙️ Config 패키지 내부 수정..."
find "$BASE_DIR/config" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.config;/package com.kafibara.chatbot.config;/g' {} \;
find "$BASE_DIR/config" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.security;/package com.kafibara.chatbot.config;/g' {} \;
find "$BASE_DIR/config" -name "*.java" -exec sed -i '' 's/^package com\.kafibara\.chatbot\.exception;/package com.kafibara.chatbot.config;/g' {} \;

# 5. 전체 프로젝트에서 크로스 패키지 import 수정
echo "🔗 크로스 패키지 import 수정..."

# User 관련 import 수정
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.entity\.User;/import com.kafibara.chatbot.user.entity.User;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.repository\.UserRepository;/import com.kafibara.chatbot.user.repository.UserRepository;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.service\.UserService;/import com.kafibara.chatbot.user.service.UserService;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.UserResponse;/import com.kafibara.chatbot.user.dto.UserResponse;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.AuthResponse;/import com.kafibara.chatbot.user.dto.AuthResponse;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.LoginRequest;/import com.kafibara.chatbot.user.dto.LoginRequest;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.SignupRequest;/import com.kafibara.chatbot.user.dto.SignupRequest;/g' {} \;

# Chat 관련 import 수정
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.entity\.Chat;/import com.kafibara.chatbot.chat.entity.Chat;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.entity\.Thread;/import com.kafibara.chatbot.chat.entity.Thread;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.repository\.ChatRepository;/import com.kafibara.chatbot.chat.repository.ChatRepository;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.repository\.ThreadRepository;/import com.kafibara.chatbot.chat.repository.ThreadRepository;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.service\.ChatService;/import com.kafibara.chatbot.chat.service.ChatService;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.ChatRequest;/import com.kafibara.chatbot.chat.dto.ChatRequest;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.ChatResponse;/import com.kafibara.chatbot.chat.dto.ChatResponse;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.AIServiceInfo;/import com.kafibara.chatbot.chat.dto.AIServiceInfo;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.ai\./import com.kafibara.chatbot.chat.ai./g' {} \;

# Feedback 관련 import 수정
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.entity\.Feedback;/import com.kafibara.chatbot.feedback.entity.Feedback;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.repository\.FeedbackRepository;/import com.kafibara.chatbot.feedback.repository.FeedbackRepository;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.service\.FeedbackService;/import com.kafibara.chatbot.feedback.service.FeedbackService;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.FeedbackRequest;/import com.kafibara.chatbot.feedback.dto.FeedbackRequest;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.FeedbackResponse;/import com.kafibara.chatbot.feedback.dto.FeedbackResponse;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.FeedbackStatusUpdateRequest;/import com.kafibara.chatbot.feedback.dto.FeedbackStatusUpdateRequest;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.dto\.FeedbackAnalyticsResponse;/import com.kafibara.chatbot.feedback.dto.FeedbackAnalyticsResponse;/g' {} \;

# Security 및 Exception 관련 import 수정
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.security\./import com.kafibara.chatbot.config./g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.exception\./import com.kafibara.chatbot.config./g' {} \;

# Config 내부에서 자기 자신 참조하는 경우 수정
find "$BASE_DIR/config" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.JwtUtil;/import com.kafibara.chatbot.config.JwtUtil;/g' {} \;
find "$BASE_DIR/config" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.config\.JwtAuthenticationFilter;/import com.kafibara.chatbot.config.JwtAuthenticationFilter;/g' {} \;

# User 패키지 내부에서 자기 자신 참조하는 경우 수정
find "$BASE_DIR/user" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.user\.dto\.\*;/import com.kafibara.chatbot.user.dto.*;/g' {} \;

echo "✅ Import 문 수정 완료!"
