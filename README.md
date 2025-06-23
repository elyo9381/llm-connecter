# 🤖 AI 챗봇 서비스 (LLM 커넥터)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://docs.docker.com/compose/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)

> **다중 AI 통합 기업급 챗봇 서비스**  
> 실시간 스트리밍, 포괄적인 사용자 관리, 고급 분석 기능을 갖춘 프로덕션 준비 완료 Spring Boot 애플리케이션

## 🌟 프로젝트 개요

이 프로젝트는 여러 AI 제공업체(Google Gemini, OpenAI, Claude)를 하나의 통합 플랫폼으로 원활하게 연결하는 정교한 AI 챗봇 서비스입니다. Spring Boot와 최신 반응형 프로그래밍 원칙으로 구축되어, 사용자 인증, 대화 관리, 피드백 시스템, 포괄적인 분석 등 기업 수준의 기능을 제공합니다.

## ✨ 주요 기능

### 🤖 **다중 AI 서비스 통합**
- **Google Gemini** - 고급 기능을 갖춘 주요 AI 서비스
- **OpenAI GPT** - 모델 선택이 가능한 대안 AI 서비스
- **Claude (Anthropic)** - 추가 AI 서비스 지원
- **팩토리 패턴** - 동적 AI 서비스 전환

### 💬 **고급 채팅 관리**
- **실시간 스트리밍** - Server-Sent Events로 라이브 AI 응답
- **스레드 관리** - 30분 규칙으로 컨텍스트 인식 대화
- **대화 기록** - 페이지네이션이 포함된 완전한 채팅 기록
- **다중 사용자 지원** - 사용자별 격리된 대화

### 🔐 **인증 및 보안**
- **JWT 인증** - 상태 비저장 토큰 기반 인증
- **역할 기반 권한** - 관리자 및 사용자 역할 관리
- **비밀번호 암호화** - BCrypt 해싱으로 안전한 저장
- **API 보안** - 보호된 엔드포인트와 권한 부여

### 👍 **피드백 및 분석 시스템**
- **사용자 피드백** - 댓글이 포함된 긍정/부정 피드백
- **피드백 분석** - 만족도 계산 및 트렌드 분석
- **상태 관리** - 대기/해결된 피드백 워크플로우
- **관리자 대시보드** - 포괄적인 피드백 관리

### 📊 **관리자 대시보드 및 보고서**
- **사용자 활동 분석** - 일일 가입, 로그인, 채팅 통계
- **CSV 내보내기** - 일일, 주간, 월간 데이터 보고서
- **시스템 모니터링** - 상태 확인 및 성능 메트릭

### 🐳 **프로덕션 준비 배포**
- **Docker Compose** - 완전한 컨테이너화된 배포
- **PostgreSQL** - 강력한 데이터 지속성
- **Redis 캐싱** - 성능 최적화
- **환경 프로파일** - 개발, Docker, 테스트 구성

## 🏗️ 시스템 아키텍처

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   프론트엔드      │    │   Spring Boot   │    │   AI 서비스     │
│   (모든 클라이언트) │◄──►│   애플리케이션    │◄──►│ Gemini/GPT/Claude│
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                    ┌─────────────────┐    ┌─────────────────┐
                    │   PostgreSQL    │    │     Redis       │
                    │   (데이터베이스)   │    │   (캐시)        │
                    └─────────────────┘    └─────────────────┘
```

## 🛠️ 기술 스택

### 백엔드 프레임워크
- **Spring Boot 3.2.0** - 메인 애플리케이션 프레임워크
- **Spring WebFlux** - 반응형 프로그래밍 지원
- **Spring Security** - 인증 및 권한 부여
- **Spring Data JPA** - 데이터베이스 작업 및 ORM
- **Java 21** - 최신 LTS Java 버전

### 데이터베이스 및 캐싱
- **PostgreSQL 15** - 주요 관계형 데이터베이스
- **Redis 7** - 인메모리 캐싱 및 세션 저장소
- **HikariCP** - 고성능 커넥션 풀링

### AI 통합
- **Google Gemini API** - 주요 AI 서비스 통합
- **OpenAI API** - GPT 모델 통합
- **Claude API** - Anthropic AI 서비스 통합
- **WebClient** - 반응형 HTTP 클라이언트

### 보안 및 모니터링
- **JWT (JSON Web Tokens)** - 상태 비저장 인증
- **BCrypt** - 비밀번호 해싱 알고리즘
- **Micrometer** - 애플리케이션 메트릭 수집
- **Spring Actuator** - 상태 모니터링

### 문서화 및 테스팅
- **Swagger/OpenAPI 3** - API 문서화
- **JUnit 5** - 단위 테스트 프레임워크
- **Testcontainers** - 통합 테스트
- **Mockito** - 모킹 프레임워크

### DevOps 및 배포
- **Docker & Docker Compose** - 컨테이너화
- **Gradle** - 빌드 자동화
- **멀티 스테이지 Docker 빌드** - 최적화된 이미지

## 🚀 시작하기

### 사전 요구사항
- **Java 21** 이상
- **Docker & Docker Compose**
- **Google Gemini API 키** (필수)

### 설치 방법

#### 1. 저장소 복제
```bash
git clone https://github.com/elyo9381/llm-connecter.git
cd llm-connecter
```

#### 2. 환경 설정
```bash
cp .env.example .env
# .env 파일에서 API 키 설정
```

필수 환경 변수:
```env
GEMINI_API_KEY=실제_gemini_api_키_입력
JWT_SECRET=안전한_jwt_시크릿_키_입력
```

#### 3. Docker 배포 (권장)
```bash
# 모든 서비스 시작
docker-compose up -d

# 서비스 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs -f chatbot-app
```

#### 4. 로컬 개발 환경
```bash
# 데모 프로파일 (H2 데이터베이스)
./gradlew bootRun --args='--spring.profiles.active=demo'

# Docker 프로파일 (PostgreSQL 필요)
./run-docker-profile.sh
```

### 서비스 접속 주소
- **메인 애플리케이션**: http://localhost:8080
- **API 문서**: http://localhost:8080/swagger-ui.html
- **상태 확인**: http://localhost:8080/api/health

## 📚 API 사용 예제

### 인증

#### 사용자 등록
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "안전한비밀번호123!",
    "name": "홍길동"
  }'
```

#### 사용자 로그인
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "안전한비밀번호123!"
  }'
```

### 채팅

#### AI와 채팅 (스트리밍)
```bash
curl -X POST http://localhost:8080/api/chats \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer 당신의_JWT_토큰" \
  -H "Accept: text/event-stream" \
  -d '{
    "question": "인공지능이 무엇인가요?",
    "aiService": "gemini",
    "model": "gemini-2.5-pro"
  }'
```

#### 채팅 기록 조회
```bash
curl -X GET "http://localhost:8080/api/chats?page=0&size=10" \
  -H "Authorization: Bearer 당신의_JWT_토큰"
```

### 피드백

#### 피드백 제출
```bash
curl -X POST http://localhost:8080/api/feedbacks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer 당신의_JWT_토큰" \
  -d '{
    "chatId": 1,
    "isPositive": true,
    "comment": "매우 도움이 되고 정확한 답변이었습니다!"
  }'
```

### 관리자 기능 (관리자 권한 필요)

#### 사용자 활동 보고서
```bash
curl -X GET http://localhost:8080/api/admin/activity-report \
  -H "Authorization: Bearer 관리자_JWT_토큰"
```

#### CSV 보고서 다운로드
```bash
curl -X GET http://localhost:8080/api/admin/reports/daily-chat \
  -H "Authorization: Bearer 관리자_JWT_토큰" \
  -o daily_report.csv
```

## 🔧 설정

### 애플리케이션 프로파일
- **`demo`** - H2 인메모리 데이터베이스를 사용한 로컬 개발
- **`docker`** - PostgreSQL을 사용한 Docker 환경
- **`test`** - 테스트 구성

### AI 서비스 설정
```yaml
ai:
  gemini:
    enabled: true
    api-key: ${GEMINI_API_KEY}
  openai:
    enabled: false
    api-key: ${OPENAI_API_KEY}
  claude:
    enabled: false
    api-key: ${CLAUDE_API_KEY}
```

## 🧪 테스팅

```bash
# 모든 테스트 실행
./gradlew test

# 통합 테스트
./gradlew integrationTest

# 테스트 커버리지
./gradlew test jacocoTestReport
```

## 📊 모니터링

### 상태 모니터링
```bash
# 기본 상태 확인
curl http://localhost:8080/api/health

# 상세 상태 정보
curl http://localhost:8080/actuator/health

# 메트릭
curl http://localhost:8080/actuator/metrics
```

### 관리자 분석 기능
- **일일 활동 보고서** - 사용자 등록, 로그인, 채팅 수
- **피드백 분석** - 만족도, 피드백 트렌드
- **CSV 데이터 내보내기** - 포괄적인 데이터 보고
- **시스템 성능** - 애플리케이션 메트릭

## 🐳 Docker 설정

### 서비스 구성

| 서비스 | 포트 | 설명 | 상태 확인 |
|---------|------|-------------|--------------|
| chatbot-app | 8080 | Spring Boot 애플리케이션 | `/api/health` |
| postgres | 5432 | PostgreSQL 데이터베이스 | `pg_isready` |
| redis | 6379 | Redis 캐시 | `redis-cli ping` |

### Docker 명령어
```bash
# 서비스 시작
docker-compose up -d

# 서비스 중지
docker-compose down

# 로그 확인
docker-compose logs -f [서비스명]

# 재빌드
docker-compose up --build -d

# 전체 정리
docker-compose down -v --rmi all
```

## 🔒 보안 기능

### 인증 및 권한
- 설정 가능한 만료 시간을 가진 JWT 토큰
- 역할 기반 접근 제어 (RBAC)
- 비밀번호 강도 검증
- BCrypt를 사용한 안전한 비밀번호 해싱

### API 보안
- 보호된 엔드포인트와 적절한 권한 부여
- CORS 설정
- 속도 제한 (설정 가능)
- 입력 검증 및 살균

### 환경 보안
- 환경 변수의 민감한 데이터
- 코드에서 외부화된 API 키
- 보안된 데이터베이스 자격 증명

## 🤝 기여하기

1. 저장소 포크
2. 기능 브랜치 생성 (`git checkout -b feature/새기능`)
3. 변경사항 작성 및 테스트 추가
4. 모든 테스트 통과 확인 (`./gradlew test`)
5. 변경사항 커밋 (`git commit -m '새 기능 추가'`)
6. 브랜치에 푸시 (`git push origin feature/새기능`)
7. Pull Request 생성

### 코드 표준
- Java 명명 규칙 준수
- 의미 있는 커밋 메시지 작성
- 공개 메서드에 JavaDoc 추가
- 80% 이상의 테스트 커버리지 유지

## 🆘 문제 해결

### 일반적인 문제

#### 포트 충돌
```bash
# 포트 사용 확인
lsof -i :8080

# docker-compose.yml에서 포트 변경
```

#### API 키 문제
- Gemini API 키 유효성 확인
- API 키 권한 및 할당량 확인
- 환경 변수 설정 확인

#### 데이터베이스 연결 문제
```bash
# PostgreSQL 컨테이너 상태 확인
docker-compose logs postgres

# 데이터베이스 연결 테스트
docker-compose exec postgres psql -U chatbot_user -d chatbot_db
```

### 도움 받기
- [API 문서](http://localhost:8080/swagger-ui.html) 확인
- 애플리케이션 로그: `docker-compose logs chatbot-app`
- 환경 설정 확인: `.env` 파일
- 데이터베이스 연결 및 마이그레이션 확인

### 성능 최적화
- Redis 캐싱 활성화
- 데이터베이스 쿼리 최적화
- 커넥션 풀링 설정
- JVM 매개변수 튜닝

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 라이선스가 부여됩니다 - 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

---

**❤️를 담아 Spring Boot로 제작 • Google Gemini AI로 구동**

*자세한 정보는 애플리케이션 실행 시 `/swagger-ui.html`에서 제공되는 API 문서를 참조하세요.*
