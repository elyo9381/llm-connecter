#!/bin/bash

# Docker 프로파일로 로컬 실행 스크립트
# Docker 없이 docker 프로파일을 사용하여 실행

echo "🚀 AI 챗봇 서비스 (Docker 프로파일) 로컬 실행"
echo "=============================================="

# PostgreSQL 실행 확인
echo "📊 PostgreSQL 상태 확인..."
if ! pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
    echo "⚠️ PostgreSQL이 실행되지 않았습니다."
    echo "PostgreSQL을 시작하거나 Docker를 사용하세요."
    echo ""
    echo "PostgreSQL 시작 방법:"
    echo "  - Homebrew: brew services start postgresql"
    echo "  - Docker: docker run -d --name postgres -e POSTGRES_DB=chatbot_db -e POSTGRES_USER=chatbot_user -e POSTGRES_PASSWORD=chatbot_password -p 5432:5432 postgres:15-alpine"
    exit 1
fi

echo "✅ PostgreSQL 실행 중"

# 환경 변수 설정
export SPRING_PROFILES_ACTIVE=docker
export GEMINI_API_KEY=${GEMINI_API_KEY:-jKjKjKjKjKjKjKjKjKjKjKjKjKj}
export JWT_SECRET=${JWT_SECRET:-mySecretKeyForJWTTokenGenerationThatShouldBeLongEnoughForSecurity}

echo "🔧 환경 변수 설정 완료"
echo "   - 프로파일: docker"
echo "   - Gemini API 키: ${GEMINI_API_KEY:0:20}..."
echo "   - JWT 시크릿: ${JWT_SECRET:0:20}..."

# 기존 서버 중지
echo ""
echo "🛑 기존 서버 중지..."
lsof -ti:8080 | xargs kill -9 2>/dev/null || echo "포트 8080 정리 완료"

# Gradle 빌드 및 실행
echo ""
echo "🔨 애플리케이션 빌드 및 실행..."
export JAVA_HOME=/Users/yowon/Library/Java/JavaVirtualMachines/azul-21.0.3/Contents/Home

./gradlew bootRun --args='--spring.profiles.active=docker' &

# 서버 시작 대기
echo ""
echo "⏳ 서버 시작 대기 중..."
sleep 15

# 헬스체크
if curl -f http://localhost:8080/api/health > /dev/null 2>&1; then
    echo "✅ AI 챗봇 서비스가 성공적으로 시작되었습니다!"
    echo ""
    echo "📋 서비스 정보:"
    echo "   - 애플리케이션: http://localhost:8080"
    echo "   - API 문서: http://localhost:8080/swagger-ui.html"
    echo "   - 헬스체크: http://localhost:8080/api/health"
    echo "   - 프로파일: docker"
    echo ""
    echo "🛑 서비스 중지: Ctrl+C 또는 kill \$(lsof -ti:8080)"
else
    echo "❌ 서비스 시작에 실패했습니다."
    echo "로그를 확인하세요."
fi
