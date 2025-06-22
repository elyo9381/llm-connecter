# 🤖 AI Chatbot Service (LLM Connector)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://docs.docker.com/compose/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **Enterprise-grade AI Chatbot Service with Multi-AI Integration**  
> Production-ready Spring Boot application supporting multiple AI services with real-time streaming, comprehensive user management, and advanced analytics.

## 🌟 Overview

This project is a sophisticated AI chatbot service that seamlessly integrates multiple AI providers (Google Gemini, OpenAI, Claude) into a single, unified platform. Built with Spring Boot and modern reactive programming principles, it offers enterprise-level features including user authentication, conversation management, feedback systems, and comprehensive analytics.

## ✨ Key Features

### 🤖 **Multi-AI Service Integration**
- **Google Gemini** - Primary AI service with advanced capabilities
- **OpenAI GPT** - Alternative AI service with model selection
- **Claude (Anthropic)** - Additional AI service support
- **Factory Pattern** - Dynamic AI service switching
- **Model Selection** - Support for different AI models per service

### 💬 **Advanced Chat Management**
- **Real-time Streaming** - Server-Sent Events (SSE) for live AI responses
- **Thread Management** - Context-aware conversations with automatic thread handling
- **30-minute Rule** - Intelligent thread continuation/creation logic
- **Conversation History** - Complete chat history with pagination
- **Multi-user Support** - Isolated conversations per user

### 🔐 **Authentication & Security**
- **JWT Authentication** - Stateless token-based authentication
- **Role-based Authorization** - Admin and User role management
- **Password Encryption** - BCrypt hashing for secure password storage
- **API Security** - Protected endpoints with proper authorization
- **Environment Variables** - Secure configuration management

### 👍 **Feedback & Analytics System**
- **User Feedback** - Positive/negative feedback with comments
- **Feedback Analytics** - Satisfaction rate calculations and trends
- **Status Management** - Pending/resolved feedback workflow
- **Admin Dashboard** - Comprehensive feedback management interface

### 📊 **Admin Dashboard & Reports**
- **User Activity Analytics** - Daily signup, login, and chat statistics
- **Feedback Management** - Review and manage user feedback
- **CSV Export** - Daily, weekly, and monthly data reports
- **System Monitoring** - Health checks and performance metrics

### 🐳 **Production-Ready Deployment**
- **Docker Compose** - Complete containerized deployment
- **PostgreSQL Database** - Robust data persistence
- **Redis Caching** - Performance optimization
- **Health Checks** - Container and application monitoring
- **Environment Profiles** - Development, Docker, and Test configurations

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Spring Boot   │    │   AI Services   │
│   (Any Client)  │◄──►│   Application   │◄──►│ Gemini/GPT/Claude│
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                    ┌─────────────────┐    ┌─────────────────┐
                    │   PostgreSQL    │    │     Redis       │
                    │   (Database)    │    │   (Cache)       │
                    └─────────────────┘    └─────────────────┘
```

### 🎯 Design Patterns
- **Factory Pattern** - AI service selection and instantiation
- **Repository Pattern** - Data access layer abstraction
- **DTO Pattern** - Data transfer between layers
- **Service Layer** - Business logic encapsulation
- **Controller Layer** - REST API endpoint management

## 🛠️ Technology Stack

### Backend Framework
- **Spring Boot 3.2.0** - Main application framework
- **Spring WebFlux** - Reactive programming support
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations and ORM
- **Java 21** - Latest LTS Java version

### Database & Caching
- **PostgreSQL 15** - Primary relational database
- **Redis 7** - In-memory caching and session storage
- **HikariCP** - High-performance connection pooling

### AI Integration
- **Google Gemini API** - Primary AI service integration
- **OpenAI API** - GPT model integration
- **Claude API** - Anthropic AI service integration
- **WebClient** - Reactive HTTP client for AI API calls

### Security & Monitoring
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password hashing algorithm
- **Micrometer** - Application metrics collection
- **Spring Actuator** - Health monitoring and management endpoints

### Documentation & Testing
- **Swagger/OpenAPI 3** - API documentation and testing interface
- **JUnit 5** - Unit testing framework
- **Testcontainers** - Integration testing with real databases
- **Mockito** - Mocking framework for unit tests

### DevOps & Deployment
- **Docker & Docker Compose** - Containerization and orchestration
- **Gradle** - Build automation and dependency management
- **Multi-stage Docker builds** - Optimized container images

## 🚀 Getting Started

### Prerequisites
- **Java 21** or higher
- **Docker & Docker Compose**
- **Google Gemini API Key** (required)
- **Git** for version control

### Installation

#### 1. Clone the Repository
```bash
git clone https://github.com/elyo9381/llm-connecter.git
cd llm-connecter
```

#### 2. Environment Configuration
```bash
# Copy the environment template
cp .env.example .env

# Edit the .env file with your configuration
nano .env
```

Required environment variables:
```env
# AI Service Configuration
GEMINI_API_KEY=your_actual_gemini_api_key_here
OPENAI_API_KEY=your_openai_api_key_here  # Optional
CLAUDE_API_KEY=your_claude_api_key_here  # Optional

# Security Configuration
JWT_SECRET=your_secure_jwt_secret_key_here

# Database Configuration (for Docker)
POSTGRES_DB=chatbot_db
POSTGRES_USER=chatbot_user
POSTGRES_PASSWORD=chatbot_password
```

#### 3. Docker Deployment (Recommended)
```bash
# Start all services (PostgreSQL + Redis + Application)
docker-compose up -d

# Check service status
docker-compose ps

# View application logs
docker-compose logs -f chatbot-app
```

#### 4. Local Development Setup
```bash
# Option 1: Demo profile (H2 in-memory database)
./gradlew bootRun --args='--spring.profiles.active=demo'

# Option 2: Docker profile (requires PostgreSQL)
./run-docker-profile.sh

# Option 3: Manual PostgreSQL setup
docker run -d --name postgres \
  -e POSTGRES_DB=chatbot_db \
  -e POSTGRES_USER=chatbot_user \
  -e POSTGRES_PASSWORD=chatbot_password \
  -p 5432:5432 postgres:15-alpine

./gradlew bootRun --args='--spring.profiles.active=docker'
```

### Service Access Points
- **Main Application**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/health
- **Actuator Endpoints**: http://localhost:8080/actuator

## 📚 API Documentation

### Authentication Endpoints

#### User Registration
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "SecurePassword123!",
    "name": "John Doe"
  }'
```

#### User Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "SecurePassword123!"
  }'
```

### Chat Management Endpoints

#### Create Chat (Streaming)
```bash
curl -X POST http://localhost:8080/api/chats \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Accept: text/event-stream" \
  -d '{
    "question": "What is artificial intelligence?",
    "aiService": "gemini",
    "model": "gemini-2.5-pro"
  }'
```

#### Get Chat History
```bash
curl -X GET "http://localhost:8080/api/chats?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Get Thread Conversations
```bash
curl -X GET http://localhost:8080/api/chats/threads/{threadId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Feedback Management

#### Submit Feedback
```bash
curl -X POST http://localhost:8080/api/feedbacks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "chatId": 1,
    "isPositive": true,
    "comment": "Very helpful and accurate response!"
  }'
```

#### Get User Feedbacks
```bash
curl -X GET "http://localhost:8080/api/feedbacks?isPositive=true" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Admin Endpoints (Admin Role Required)

#### User Activity Report
```bash
curl -X GET http://localhost:8080/api/admin/activity-report \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

#### Feedback Analytics
```bash
curl -X GET http://localhost:8080/api/admin/feedback-analytics \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

#### CSV Reports
```bash
# Daily report
curl -X GET http://localhost:8080/api/admin/reports/daily-chat \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -o daily_report.csv

# Weekly report
curl -X GET http://localhost:8080/api/admin/reports/weekly-chat \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -o weekly_report.csv
```

## 🔧 Configuration

### Application Profiles

The application supports multiple profiles for different environments:

- **`demo`** - Local development with H2 in-memory database
- **`docker`** - Docker environment with PostgreSQL
- **`test`** - Testing environment with test configurations

### AI Service Configuration

```yaml
ai:
  gemini:
    enabled: true
    api-key: ${GEMINI_API_KEY}
    base-url: https://generativelanguage.googleapis.com
  
  openai:
    enabled: false
    api-key: ${OPENAI_API_KEY}
    base-url: https://api.openai.com/v1
  
  claude:
    enabled: false
    api-key: ${CLAUDE_API_KEY}
    base-url: https://api.anthropic.com
```

### Database Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/chatbot_db
    username: ${POSTGRES_USER:chatbot_user}
    password: ${POSTGRES_PASSWORD:chatbot_password}
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

## 🧪 Testing

### Running Tests
```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run integration tests
./gradlew integrationTest

# Run specific test class
./gradlew test --tests "ChatServiceTest"
```

### Test Coverage
The project includes comprehensive testing:
- **Unit Tests** - Service layer business logic
- **Integration Tests** - API endpoints with Testcontainers
- **Security Tests** - Authentication and authorization flows

## 📊 Monitoring & Analytics

### Health Monitoring
```bash
# Basic health check
curl http://localhost:8080/api/health

# Detailed health information
curl http://localhost:8080/actuator/health

# Application metrics
curl http://localhost:8080/actuator/metrics
```

### Admin Analytics Features
- **Daily Activity Reports** - User registrations, logins, chat counts
- **Feedback Analytics** - Satisfaction rates, feedback trends
- **CSV Data Exports** - Comprehensive data reporting
- **System Performance** - Application metrics and monitoring

## 🐳 Docker Configuration

### Services Overview

| Service | Port | Description | Health Check |
|---------|------|-------------|--------------|
| chatbot-app | 8080 | Spring Boot Application | `/api/health` |
| postgres | 5432 | PostgreSQL Database | `pg_isready` |
| redis | 6379 | Redis Cache | `redis-cli ping` |

### Docker Commands

```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f [service-name]

# Rebuild and restart
docker-compose up --build -d

# Scale services (if needed)
docker-compose up -d --scale chatbot-app=2

# Clean up everything
docker-compose down -v --rmi all
```

### Production Deployment

For production deployment, consider:
- Using external managed databases (AWS RDS, etc.)
- Implementing load balancing
- Setting up monitoring and alerting
- Configuring backup strategies
- Using secrets management services

## 🔒 Security Considerations

### Authentication & Authorization
- JWT tokens with configurable expiration
- Role-based access control (RBAC)
- Password strength validation
- Secure password hashing with BCrypt

### API Security
- Protected endpoints with proper authorization
- CORS configuration for cross-origin requests
- Rate limiting (configurable)
- Input validation and sanitization

### Environment Security
- Sensitive data in environment variables
- API keys externalized from code
- Database credentials secured
- Docker secrets support

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### Development Setup
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass (`./gradlew test`)
6. Commit your changes (`git commit -m 'Add amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

### Code Standards
- Follow Java naming conventions
- Write meaningful commit messages
- Add JavaDoc for public methods
- Maintain test coverage above 80%
- Use consistent code formatting

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support & Troubleshooting

### Common Issues

#### Port Conflicts
```bash
# Check what's using port 8080
lsof -i :8080

# Change port in docker-compose.yml or application.yml
```

#### API Key Issues
- Verify your Gemini API key is valid
- Check API key permissions and quotas
- Ensure environment variables are properly set

#### Database Connection Issues
```bash
# Check PostgreSQL container status
docker-compose logs postgres

# Test database connection
docker-compose exec postgres psql -U chatbot_user -d chatbot_db
```

#### Memory Issues
- Increase Docker memory allocation
- Monitor application memory usage
- Consider JVM tuning for production

### Getting Help
- Check the [API Documentation](http://localhost:8080/swagger-ui.html)
- Review application logs: `docker-compose logs chatbot-app`
- Verify environment configuration in `.env`
- Check database connectivity and migrations

### Performance Optimization
- Enable Redis caching for frequently accessed data
- Optimize database queries and indexes
- Configure connection pooling
- Monitor and tune JVM parameters

---

**Built with ❤️ using Spring Boot • Powered by Google Gemini AI**

*For more detailed information, please refer to the API documentation available at `/swagger-ui.html` when the application is running.*
