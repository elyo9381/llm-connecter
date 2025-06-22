# 🤖 AI 챗봇 서비스

> 다중 AI 통합 기업급 챗봇 서비스

## 주요 기능
- 🤖 다중 AI 지원 (Gemini, OpenAI, Claude)
- 💬 실시간 스트리밍 채팅
- 🔐 JWT 인증 및 권한 관리
- 📊 스레드 관리 및 분석
- 👍 피드백 시스템
- 📈 관리자 대시보드
- 🐳 Docker 배포

## 빠른 시작
```bash
git clone https://github.com/elyo9381/llm-connecter.git
cd llm-connecter
cp .env.example .env
# .env에서 GEMINI_API_KEY 설정
docker-compose up -d
```

## 접속 주소
- 앱: http://localhost:8080
- API 문서: http://localhost:8080/swagger-ui.html
- 상태 확인: http://localhost:8080/api/health

## 기술 스택
Spring Boot 3.2.0 • PostgreSQL • Redis • Docker

## API 예제

### 회원가입
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"Pass123!","name":"홍길동"}'
```

### AI 채팅
```bash
curl -X POST http://localhost:8080/api/chats \
  -H "Authorization: Bearer JWT_토큰" \
  -d '{"question":"인공지능이 무엇인가요?","aiService":"gemini"}'
```

## 개발 환경
```bash
# 로컬 개발
./gradlew bootRun --args='--spring.profiles.active=demo'

# 테스트
./gradlew test
```

## Docker 서비스

| 서비스 | 포트 | 설명 |
|---------|------|-------------|
| chatbot-app | 8080 | Spring Boot 애플리케이션 |
| postgres | 5432 | PostgreSQL 데이터베이스 |
| redis | 6379 | Redis 캐시 |

## 라이선스
MIT
