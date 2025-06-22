# 멀티 스테이지 빌드
FROM gradle:8.5-jdk21 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 설정 파일들 복사
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew ./
COPY gradlew.bat ./

# 의존성 다운로드 (캐시 최적화)
RUN gradle dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN gradle bootJar --no-daemon

# 실행 스테이지
FROM eclipse-temurin:21-jre

# 작업 디렉토리 설정
WORKDIR /app

# curl 설치 (헬스체크용)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 헬스체크 추가
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
