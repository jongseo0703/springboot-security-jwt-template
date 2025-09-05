# UserTemplate

Spring Boot 기반 사용자 인증/인가 템플릿 프로젝트입니다.  
JWT + Redis를 활용한 일반 로그인 기능과 Swagger를 통한 API 테스트를 지원합니다.

---

## 🛠 기술 스택

- Java 21
- Spring Boot 3.5.5
- Spring Security
- Spring Data JPA
- Spring Data Redis
- JWT (io.jsonwebtoken)
- MySQL / H2 (개발용)
- Swagger/OpenAPI (`springdoc-openapi-starter-webmvc-ui`)

---

## 📦 프로젝트 구조
├── build.gradle
├── gradlew
├── settings.gradle
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── usertemplate
│   │   │               ├── UsertemplateApplication.java # Spring Boot main application class
│   │   │               ├── auth
│   │   │               │   ├── controller
│   │   │               │   │   └── AuthController.java      # Controller for authentication APIs (login, register)
│   │   │               │   ├── dto
│   │   │               │   │   ├── LoginRequest.java
│   │   │               │   │   ├── LoginResponse.java
│   │   │               │   │   └── RegisterRequest.java
│   │   │               │   ├── security
│   │   │               │   │   ├── JwtAccessDeniedHandler.java
│   │   │               │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │               │   │   ├── JwtAuthenticationFilter.java
│   │   │               │   │   └── JwtTokenProvider.java    # JWT token generation and validation
│   │   │               │   └── service
│   │   │               │       ├── AuthService.java
│   │   │               │       └── AuthServiceImpl.java     # Business logic for authentication
│   │   │               ├── global
│   │   │               │   ├── common
│   │   │               │   │   ├── ApiResponse.java       # Standard API response format
│   │   │               │   │   └── BaseEntity.java        # Base entity with created/updated dates
│   │   │               │   ├── config
│   │   │               │   │   ├── JpaConfig.java
│   │   │               │   │   ├── OpenApiConfig.java     # Swagger/OpenAPI configuration
│   │   │               │   │   └── SecurityConfig.java    # Spring Security configuration
│   │   │               │   ├── exception
│   │   │               │   │   ├── BusinessException.java
│   │   │               │   │   ├── ErrorResponse.java
│   │   │               │   │   └── GlobalExceptionHandler.java # Global exception handling
│   │   │               │   └── redis
│   │   │               │       ├── Redis.java
│   │   │               │       ├── RedisConfig.java
│   │   │               │       ├── RedisProperties.java
│   │   │               │       └── RedisUtil.java         # Redis utility class
│   │   │               └── user
│   │   │                   ├── dto
│   │   │                   │   ├── UserResponse.java
│   │   │                   │   └── UserUpdateRequest.java
│   │   │                   ├── entity
│   │   │                   │   ├── Role.java
│   │   │                   │   └── User.java              # User entity
│   │   │                   ├── repository
│   │   │                   │   └── UserRepository.java    # JPA repository for User
│   │   │                   └── service
│   │   │                       ├── UserService.java
│   │   │                       └── UserServiceImpl.java   # Business logic for user operations
│   │   └── resources
│   │       ├── application.properties                     # Application configuration
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── usertemplate
│                       └── UsertemplateApplicationTests.java
└── .github
    ├── Branch&GitConvention.md
    └── pull_request_template.md

## 기능

### 회원가입
- `POST /auth/register`
- username, email, password를 받아 신규 사용자 등록
- username/email 중복 시 예외 발생

### 로그인
- `POST /auth/login`
- username, password로 로그인
- JWT Access Token / Refresh Token 발급
- Refresh Token Redis에 저장 (TTL 14일)

### JWT 토큰
- Access Token: 인증 헤더(`Authorization: Bearer {token}`)로 요청
- Refresh Token: Redis에서 관리
- Spring Security와 연동하여 인증/인가 처리

### Redis
- 로그인 시 Refresh Token을 `username` 또는 `email` 키로 저장
- TTL 14일
- `RedisUtil` 클래스에서 관리

---

## 환경 설정

### application.properties

```properties
# Spring Data Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT
jwt.secret=mySecretKeyForJwtTokenGenerationAndValidation
jwt.expiration=86400000           # Access Token 만료 1일
jwt.refresh-expiration=1209600000 # Refresh Token 만료 14일

## Redis 설치
brew install redis

## 프로젝트 실행 방법
1. Redis 서버 실행
redis-server
2. Spring Boot 애플리케이션 실행
./gradlew bootRun
3. Swagger UI 접속
http://localhost:8080/swagger-ui.html
