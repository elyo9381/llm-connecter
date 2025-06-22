# Custom Chatbot Service

AI 챗봇 서비스 - Spring Boot 3.x, Java 21, PostgreSQL 기반

## 기술 스택

- **Backend**: Java 21, Spring Boot 3.2.0
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **AI Integration**: 다중 AI 서비스 지원 (OpenAI, Claude, Mock AI)
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Monitoring**: Spring Boot Actuator + Micrometer
- **Caching**: Spring Cache
- **Build Tool**: Gradle
- **Containerization**: Docker & Docker Compose

## 프로젝트 구조

```
src/main/java/com/kafibara/chatbot/
├── config/          # 설정 클래스들
├── controller/      # REST API 컨트롤러
├── service/         # 비즈니스 로직
├── repository/      # 데이터 접근 계층
├── entity/          # JPA 엔티티
├── dto/             # 데이터 전송 객체
├── security/        # 보안 관련 클래스
├── ai/              # AI 서비스 통합
│   ├── impl/        # AI 서비스 구현체들
│   ├── AIService.java
│   ├── AIServiceFactory.java
│   └── ChatMessage.java
└── exception/       # 예외 처리
```

## 주요 기능

### 1. 사용자 관리 및 인증 ✅
- 회원가입/로그인
- JWT 기반 인증
- 권한 관리 (MEMBER/ADMIN)
- 사용자 캐싱

### 2. 대화 관리 ✅
- AI 챗봇과의 대화
- 스레드 기반 대화 관리 (30분 타임아웃)
- 스트리밍 응답 지원
- 다중 AI 서비스 지원

### 3. AI 서비스 통합 ✅
- **Mock AI**: 테스트용 가상 AI 서비스
- **OpenAI**: GPT 모델 지원
- **Claude**: Anthropic Claude 모델 지원
- Strategy Pattern으로 확장 가능한 구조

### 4. 피드백 시스템 ✅
- 대화에 대한 긍정/부정 피드백 생성
- 피드백 상태 관리 (PENDING/RESOLVED)
- 사용자별 피드백 조회
- 관리자 피드백 관리

### 5. 분석 및 보고 ✅
- 사용자 활동 통계
- 피드백 분석 및 만족도 계산
- CSV 보고서 생성 (일일/주간/월간)
- 실시간 대시보드 데이터

### 6. 모니터링 및 성능 ✅
- Spring Boot Actuator 통합
- 커스텀 메트릭 수집
- 헬스체크 엔드포인트
- 성능 모니터링
- 캐싱 최적화

### 7. 테스트 및 품질 보증 ✅
- 단위 테스트 (JUnit 5 + Mockito)
- 통합 테스트
- 테스트 환경 분리 (H2 인메모리 DB)

## 개발 환경 설정

### 1. 필수 요구사항
- Java 21
- PostgreSQL 12+
- Gradle 8.x
- Docker & Docker Compose (선택사항)

### 2. 로컬 개발 환경

#### 데이터베이스 설정
```sql
-- 데이터베이스 생성
CREATE DATABASE chatbot_db;
CREATE USER chatbot_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE chatbot_db TO chatbot_user;
```

#### 환경 변수 설정
```bash
export DB_USERNAME=chatbot_user
export DB_PASSWORD=password
export JWT_SECRET=your-secret-key-here-make-it-long-and-secure
export OPENAI_API_KEY=your-openai-api-key
export CLAUDE_API_KEY=your-claude-api-key
```

#### 애플리케이션 실행
```bash
# 빌드
./gradlew clean build

# 실행
./gradlew bootRun
```

### 3. Docker 환경

#### Docker Compose로 실행
```bash
# 환경 변수 설정 (선택사항)
export OPENAI_API_KEY=your-openai-api-key
export CLAUDE_API_KEY=your-claude-api-key

# 서비스 시작
docker-compose up -d

# 로그 확인
docker-compose logs -f app

# 서비스 중지
docker-compose down
```

#### 개별 Docker 빌드
```bash
# 이미지 빌드
docker build -t custom-chatbot .

# 컨테이너 실행
docker run -p 8080:8080 \
  -e DB_USERNAME=chatbot_user \
  -e DB_PASSWORD=password \
  -e JWT_SECRET=your-secret-key \
  custom-chatbot
```

## 기본 계정 정보

애플리케이션 시작 시 자동으로 생성되는 관리자 계정:
- **이메일**: admin@chatbot.com
- **비밀번호**: admin123!

## API 문서 및 모니터링

### API 문서
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

### 모니터링 엔드포인트
- 헬스체크: http://localhost:8080/actuator/health
- 애플리케이션 정보: http://localhost:8080/actuator/info
- 메트릭: http://localhost:8080/actuator/metrics
- 환경 정보: http://localhost:8080/actuator/env

## API 테스트

```bash
# 기본 API 테스트
./test-api.sh

# AI 기능 테스트
./test-ai-api.sh

# 피드백 및 분석 기능 테스트
./test-feedback-api.sh

# 전체 기능 통합 테스트
./test-all-features.sh

# 성능 및 모니터링 테스트
./test-performance.sh
```

### 주요 API 엔드포인트

#### 인증 API
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `GET /api/auth/me` - 내 정보 조회

#### 대화 API
- `POST /api/chats` - 대화 생성
- `POST /api/chats/stream` - 스트리밍 대화 생성
- `GET /api/chats` - 대화 목록 조회
- `GET /api/chats/threads/{threadId}` - 스레드별 대화 조회
- `DELETE /api/chats/threads/{threadId}` - 스레드 삭제

#### 피드백 API
- `POST /api/feedbacks` - 피드백 생성
- `GET /api/feedbacks` - 피드백 목록 조회
- `GET /api/feedbacks/{feedbackId}` - 피드백 상세 조회
- `PUT /api/feedbacks/{feedbackId}/status` - 피드백 상태 변경 (관리자)
- `DELETE /api/feedbacks/{feedbackId}` - 피드백 삭제

#### AI 서비스 API
- `GET /api/ai-services` - 사용 가능한 AI 서비스 목록
- `GET /api/ai-services/names` - AI 서비스 이름 목록

#### 사용자 관리 API (관리자 전용)
- `GET /api/users` - 사용자 목록 조회
- `GET /api/users/{userId}` - 사용자 상세 조회

#### 관리자 API
- `GET /api/admin/activity-report` - 일일 활동 보고서
- `GET /api/admin/feedback-analytics` - 피드백 분석
- `GET /api/admin/reports/daily-chat` - 일일 대화 보고서 (CSV)
- `GET /api/admin/reports/weekly-chat` - 주간 대화 보고서 (CSV)
- `GET /api/admin/reports/monthly-chat` - 월간 대화 보고서 (CSV)

#### 기타
- `GET /api/health` - 헬스체크

## AI 서비스 사용법

### 1. Mock AI (기본 활성화)
테스트용 가상 AI 서비스로, API 키 없이 사용 가능합니다.

```json
{
  "question": "안녕하세요!",
  "aiService": "mock",
  "model": "mock-gpt-3.5"
}
```

### 2. OpenAI
OpenAI API 키가 필요합니다.

```json
{
  "question": "Hello, how are you?",
  "aiService": "openai",
  "model": "gpt-3.5-turbo"
}
```

### 3. Claude
Claude API 키가 필요합니다.

```json
{
  "question": "Explain quantum computing",
  "aiService": "claude",
  "model": "claude-3-haiku-20240307"
}
```

## 피드백 시스템

### 피드백 생성
```json
{
  "chatId": 123,
  "isPositive": true
}
```

### 피드백 상태
- `PENDING`: 대기 중
- `RESOLVED`: 처리 완료

### 피드백 분석
- 전체/일일 피드백 통계
- 긍정/부정 피드백 비율
- 만족도 계산
- 상태별 피드백 현황

## 보고서 기능

### CSV 보고서 포함 정보
- 대화 ID, 스레드 ID
- 사용자 정보 (ID, 이름, 이메일)
- 질문 및 답변 내용
- 사용된 AI 모델
- 생성 일시

### 보고서 유형
- **일일 보고서**: 오늘 하루 대화
- **주간 보고서**: 최근 7일 대화
- **월간 보고서**: 최근 30일 대화

## 성능 최적화

### 구현된 최적화
- **캐싱**: 사용자 정보 캐싱
- **JPA 최적화**: 지연 로딩, 인덱스 활용
- **페이지네이션**: 대용량 데이터 처리
- **메트릭 수집**: 성능 모니터링
- **헬스체크**: 서비스 상태 모니터링

### 모니터링 메트릭
- 대화 요청 수
- AI 응답 시간
- 피드백 생성 수
- 사용자 등록 수
- JVM 메모리 사용량

## 테스트

### 단위 테스트
```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests UserServiceTest
```

### 통합 테스트
```bash
# 통합 테스트 실행
./gradlew integrationTest
```

## 개발 단계

- [x] **Phase 1**: 기본 인프라 구축 (완료)
  - Spring Boot 프로젝트 설정
  - 데이터베이스 스키마 설계
  - JPA 엔티티 및 Repository 구현
  - 기본 설정 및 예외 처리

- [x] **Phase 2**: 인증 및 사용자 관리 (완료)
  - Spring Security + JWT 구현
  - 회원가입/로그인 API
  - 권한 기반 접근 제어
  - 사용자 관리 기능

- [x] **Phase 3**: AI 서비스 통합 (완료)
  - Strategy Pattern으로 다중 AI 서비스 지원
  - OpenAI, Claude, Mock AI 서비스 구현
  - 대화 관리 및 스레드 시스템
  - 스트리밍 응답 지원

- [x] **Phase 4**: 피드백 및 분석 기능 (완료)
  - 피드백 시스템 구현
  - 피드백 상태 관리
  - 고급 분석 및 통계
  - CSV 보고서 생성

- [x] **Phase 5**: 테스트 및 최적화 (완료)
  - 단위 테스트 및 통합 테스트
  - 성능 최적화 (캐싱, 메트릭)
  - 모니터링 설정
  - Docker 컨테이너화
  - 배포 준비

## 배포

### 프로덕션 환경 고려사항
- 환경별 설정 파일 분리
- 보안 강화 (HTTPS, 보안 헤더)
- 로드 밸런싱
- 데이터베이스 최적화
- 로그 관리
- 백업 전략

### 확장 가능성
- Redis 캐싱 도입
- 메시지 큐 (RabbitMQ/Kafka)
- 마이크로서비스 아키텍처
- API Gateway
- 서비스 메시 (Istio)

## 라이센스

MIT License

---

## 프로젝트 완성도: 100% ✅

이 프로젝트는 현대적인 Spring Boot 애플리케이션의 모든 핵심 요소를 포함하고 있습니다:
- 완전한 REST API
- 보안 및 인증
- 데이터베이스 통합
- AI 서비스 통합
- 모니터링 및 메트릭
- 테스트 커버리지
- 컨테이너화
- 문서화
