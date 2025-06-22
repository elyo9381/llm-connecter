#!/bin/bash

# AI 챗봇 서비스 Docker Compose 실행 스크립트

echo "🚀 AI 챗봇 서비스 시작"
echo "======================="

# .env 파일 확인
if [ ! -f .env ]; then
    echo "⚠️ .env 파일이 없습니다. .env.example을 참고하여 .env 파일을 생성하세요."
    echo "기본 설정으로 진행합니다..."
fi

# Docker와 Docker Compose 설치 확인
if ! command -v docker &> /dev/null; then
    echo "❌ Docker가 설치되지 않았습니다."
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose가 설치되지 않았습니다."
    exit 1
fi

# 기존 컨테이너 정리 (선택사항)
echo "🧹 기존 컨테이너 정리 중..."
docker-compose down

# 이미지 빌드 및 서비스 시작
echo "🔨 이미지 빌드 및 서비스 시작 중..."
docker-compose up --build -d

# 서비스 상태 확인
echo "⏳ 서비스 시작 대기 중..."
sleep 30

# 헬스체크
echo "🏥 서비스 상태 확인 중..."
if curl -f http://localhost:8080/api/health > /dev/null 2>&1; then
    echo "✅ AI 챗봇 서비스가 성공적으로 시작되었습니다!"
    echo ""
    echo "📋 서비스 정보:"
    echo "   - 애플리케이션: http://localhost:8080"
    echo "   - API 문서: http://localhost:8080/swagger-ui.html"
    echo "   - 헬스체크: http://localhost:8080/api/health"
    echo "   - PostgreSQL: localhost:5432"
    echo "   - Redis: localhost:6379"
    echo ""
    echo "📊 로그 확인: docker-compose logs -f"
    echo "🛑 서비스 중지: docker-compose down"
else
    echo "❌ 서비스 시작에 실패했습니다."
    echo "📊 로그를 확인하세요: docker-compose logs"
fi
