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
├── build.gradle                                                                                                                                            │
 │    10 ├── gradlew                                                                                                                                                 │
 │    11 ├── settings.gradle                                                                                                                                         │
 │    12 ├── src                                                                                                                                                     │
 │    13 │   ├── main                                                                                                                                                │
 │    14 │   │   ├── java                                                                                                                                            │
 │    15 │   │   │   └── com                                                                                                                                         │
 │    16 │   │   │       └── example                                                                                                                                 │
 │    17 │   │   │           └── usertemplate                                                                                                                        │
 │    18 │   │   │               ├── UsertemplateApplication.java # Spring Boot main application class                                                               │
 │    19 │   │   │               ├── auth                                                                                                                            │
 │    20 │   │   │               │   ├── controller                                                                                                                  │
 │    21 │   │   │               │   │   └── AuthController.java      # Controller for authentication APIs (login, register)                                         │
 │    22 │   │   │               │   ├── dto                                                                                                                         │
 │    23 │   │   │               │   │   ├── LoginRequest.java                                                                                                       │
 │    24 │   │   │               │   │   ├── LoginResponse.java                                                                                                      │
 │    25 │   │   │               │   │   └── RegisterRequest.java                                                                                                    │
 │    26 │   │   │               │   ├── security                                                                                                                    │
 │    27 │   │   │               │   │   ├── JwtAccessDeniedHandler.java                                                                                             │
 │    28 │   │   │               │   │   ├── JwtAuthenticationEntryPoint.java                                                                                        │
 │    29 │   │   │               │   │   ├── JwtAuthenticationFilter.java                                                                                            │
 │    30 │   │   │               │   │   └── JwtTokenProvider.java    # JWT token generation and validation                                                          │
 │    31 │   │   │               │   └── service                                                                                                                     │
 │    32 │   │   │               │       ├── AuthService.java                                                                                                        │
 │    33 │   │   │               │       └── AuthServiceImpl.java     # Business logic for authentication                                                            │
 │    34 │   │   │               ├── global                                                                                                                          │
 │    35 │   │   │               │   ├── common                                                                                                                      │
 │    36 │   │   │               │   │   ├── ApiResponse.java       # Standard API response format                                                                   │
 │    37 │   │   │               │   │   └── BaseEntity.java        # Base entity with created/updated dates                                                         │
 │    38 │   │   │               │   ├── config                                                                                                                      │
 │    39 │   │   │               │   │   ├── JpaConfig.java                                                                                                          │
 │    40 │   │   │               │   │   ├── OpenApiConfig.java     # Swagger/OpenAPI configuration                                                                  │
 │    41 │   │   │               │   │   └── SecurityConfig.java    # Spring Security configuration                                                                  │
 │    42 │   │   │               │   ├── exception                                                                                                                   │
 │    43 │   │   │               │   │   ├── BusinessException.java                                                                                                  │
 │    44 │   │   │               │   │   ├── ErrorResponse.java                                                                                                      │
 │    45 │   │   │               │   │   └── GlobalExceptionHandler.java # Global exception handling                                                                 │
 │    46 │   │   │               │   └── redis                                                                                                                       │
 │    47 │   │   │               │       ├── Redis.java                                                                                                              │
 │    48 │   │   │               │       ├── RedisConfig.java                                                                                                        │
 │    49 │   │   │               │       ├── RedisProperties.java                                                                                                    │
 │    50 │   │   │               │       └── RedisUtil.java         # Redis utility class                                                                            │
 │    51 │   │   │               └── user                                                                                                                            │
 │    52 │   │   │                   ├── dto                                                                                                                         │
 │    53 │   │   │                   │   ├── UserResponse.java                                                                                                       │
 │    54 │   │   │                   │   └── UserUpdateRequest.java                                                                                                  │
 │    55 │   │   │                   ├── entity                                                                                                                      │
 │    56 │   │   │                   │   ├── Role.java                                                                                                               │
 │    57 │   │   │                   │   └── User.java              # User entity                                                                                    │
 │    58 │   │   │                   ├── repository                                                                                                                  │
 │    59 │   │   │                   │   └── UserRepository.java    # JPA repository for User                                                                        │
 │    60 │   │   │                   └── service                                                                                                                     │
 │    61 │   │   │                       ├── UserService.java                                                                                                        │
 │    62 │   │   │                       └── UserServiceImpl.java   # Business logic for user operations                                                             │
 │    63 │   │   └── resources                                                                                                                                       │
 │    64 │   │       ├── application.properties                     # Application configuration                                                                      │
 │    65 │   │       ├── static                                                                                                                                      │
 │    66 │   │       └── templates                                                                                                                                   │
 │    67 │   └── test                                                                                                                                                │
 │    68 │       └── java                                                                                                                                            │
 │    69 │           └── com                                                                                                                                         │
 │    70 │               └── example                                                                                                                                 │
 │    71 │                   └── usertemplate                                                                                                                        │
 │    72 │                       └── UsertemplateApplicationTests.java                                                                                               │
 │    73 └── .github                                                                                                                                                 │
 │    74     ├── Branch&GitConvention.md                                                                                                                             │
 │    75     └── pull_request_template.md

---

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
