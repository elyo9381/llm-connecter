#!/bin/bash

# 패키지 재구성 후 import 문 업데이트 스크립트

BASE_DIR="/Users/yowon/study/custom-chatbot/custom-chatbot/src/main/java/com/kafibara/chatbot"

echo "🔄 패키지 선언 및 import 문 업데이트 시작..."

# User 패키지 파일들 업데이트
echo "📁 User 패키지 업데이트..."
find "$BASE_DIR/user" -name "*.java" -exec sed -i '' 's/package com\.kafibara\.chatbot\.\(controller\|service\|entity\|repository\|dto\);/package com.kafibara.chatbot.user.\1;/g' {} \;
find "$BASE_DIR/user" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.\(dto\|entity\|service\|repository\)\./import com.kafibara.chatbot.user.\1./g' {} \;

# Chat 패키지 파일들 업데이트
echo "💬 Chat 패키지 업데이트..."
find "$BASE_DIR/chat" -name "*.java" -exec sed -i '' 's/package com\.kafibara\.chatbot\.\(controller\|service\|entity\|repository\|dto\|ai\);/package com.kafibara.chatbot.chat.\1;/g' {} \;
find "$BASE_DIR/chat" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.\(dto\|entity\|service\|repository\|ai\)\./import com.kafibara.chatbot.chat.\1./g' {} \;

# Feedback 패키지 파일들 업데이트
echo "📝 Feedback 패키지 업데이트..."
find "$BASE_DIR/feedback" -name "*.java" -exec sed -i '' 's/package com\.kafibara\.chatbot\.\(controller\|service\|entity\|repository\|dto\);/package com.kafibara.chatbot.feedback.\1;/g' {} \;
find "$BASE_DIR/feedback" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.\(dto\|entity\|service\|repository\)\./import com.kafibara.chatbot.feedback.\1./g' {} \;

# Config 패키지 파일들 업데이트
echo "⚙️ Config 패키지 업데이트..."
find "$BASE_DIR/config" -name "*.java" -exec sed -i '' 's/package com\.kafibara\.chatbot\.\(config\|security\|exception\);/package com.kafibara.chatbot.config;/g' {} \;

# 크로스 패키지 import 업데이트
echo "🔗 크로스 패키지 import 업데이트..."

# User 관련 import 업데이트
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.entity\.User;/import com.kafibara.chatbot.user.entity.User;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.repository\.UserRepository;/import com.kafibara.chatbot.user.repository.UserRepository;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.service\.UserService;/import com.kafibara.chatbot.user.service.UserService;/g' {} \;

# Chat 관련 import 업데이트
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.entity\.\(Chat\|Thread\);/import com.kafibara.chatbot.chat.entity.\1;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.repository\.\(Chat\|Thread\)Repository;/import com.kafibara.chatbot.chat.repository.\1Repository;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.service\.ChatService;/import com.kafibara.chatbot.chat.service.ChatService;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.ai\./import com.kafibara.chatbot.chat.ai./g' {} \;

# Feedback 관련 import 업데이트
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.entity\.Feedback;/import com.kafibara.chatbot.feedback.entity.Feedback;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.repository\.FeedbackRepository;/import com.kafibara.chatbot.feedback.repository.FeedbackRepository;/g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.service\.FeedbackService;/import com.kafibara.chatbot.feedback.service.FeedbackService;/g' {} \;

# Security 관련 import 업데이트
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.security\./import com.kafibara.chatbot.config./g' {} \;
find "$BASE_DIR" -name "*.java" -exec sed -i '' 's/import com\.kafibara\.chatbot\.exception\./import com.kafibara.chatbot.config./g' {} \;

echo "✅ 패키지 업데이트 완료!"
