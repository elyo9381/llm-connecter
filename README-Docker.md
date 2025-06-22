# AI 챗봇 서비스 - Docker 실행 가이드

## 🚀 빠른 시작

### 1. 환경 변수 설정
```bash
# .env 파일 생성
cp .env.example .env

# Gemini API 키 설정 (필수)
# .env 파일을 열어서 실제 API 키로 변경하세요
GEMINI_API_KEY=your_actual_gemini_api_key_here
```

### 2. Docker Compose로 실행
```bash
# 간단한 실행
./start.sh

# 또는 직접 실행
docker-compose up --build -d
```

### 3. 서비스 확인
- **애플리케이션**: http://localhost:8080
- **API 문서**: http://localhost:8080/swagger-ui.html
- **헬스체크**: http://localhost:8080/api/health

## 📋 서비스 구성

### 컨테이너
- **chatbot-app**: AI 챗봇 애플리케이션 (포트: 8080)
- **postgres**: PostgreSQL 데이터베이스 (포트: 5432)
- **redis**: Redis 캐시 (포트: 6379)

### 네트워크
- **chatbot-network**: 내부 Docker 네트워크

### 볼륨
- **postgres_data**: PostgreSQL 데이터 영구 저장

## 🛠️ 관리 명령어

### 서비스 시작
```bash
docker-compose up -d
```

### 서비스 중지
```bash
docker-compose down
```

### 로그 확인
```bash
# 모든 서비스 로그
docker-compose logs -f

# 특정 서비스 로그
docker-compose logs -f chatbot-app
docker-compose logs -f postgres
```

### 서비스 상태 확인
```bash
docker-compose ps
```

### 데이터베이스 접속
```bash
docker-compose exec postgres psql -U chatbot_user -d chatbot_db
```

### 애플리케이션 컨테이너 접속
```bash
docker-compose exec chatbot-app bash
```

## 🔧 개발 모드

### 코드 변경 후 재빌드
```bash
docker-compose up --build -d chatbot-app
```

### 데이터베이스 초기화
```bash
docker-compose down -v  # 볼륨까지 삭제
docker-compose up -d
```

## 📊 모니터링

### 헬스체크
```bash
curl http://localhost:8080/api/health
```

### 메트릭스
```bash
curl http://localhost:8080/actuator/metrics
```

## 🐛 문제 해결

### 포트 충돌
```bash
# 사용 중인 포트 확인
lsof -i :8080
lsof -i :5432

# 포트 변경 (docker-compose.yml 수정)
```

### 메모리 부족
```bash
# Docker 메모리 설정 확인
docker system df
docker system prune
```

### 데이터베이스 연결 오류
```bash
# PostgreSQL 컨테이너 상태 확인
docker-compose logs postgres

# 데이터베이스 연결 테스트
docker-compose exec postgres pg_isready -U chatbot_user
```

## 🔒 보안 설정

### 프로덕션 환경
1. `.env` 파일의 비밀번호 변경
2. JWT 시크릿 키 변경
3. 외부 접근 포트 제한
4. HTTPS 설정

### 환경 변수
```bash
# 필수 환경 변수
GEMINI_API_KEY=실제_API_키
JWT_SECRET=강력한_시크릿_키
POSTGRES_PASSWORD=강력한_비밀번호
```

## 📈 성능 최적화

### JVM 옵션
```yaml
# docker-compose.yml에서 JVM 옵션 추가
environment:
  - JAVA_OPTS=-Xmx1g -Xms512m
```

### 데이터베이스 튜닝
```yaml
# PostgreSQL 설정 최적화
environment:
  - POSTGRES_SHARED_BUFFERS=256MB
  - POSTGRES_EFFECTIVE_CACHE_SIZE=1GB
```

## 🎯 API 테스트

### 회원가입
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test123!",
    "name": "테스트 사용자"
  }'
```

### 대화 생성
```bash
curl -X POST http://localhost:8080/api/chats \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "question": "안녕하세요",
    "aiService": "gemini",
    "model": "gemini-2.5-pro"
  }'
```
