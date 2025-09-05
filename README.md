# User Template Project

Spring Boot 기반 사용자 인증/인가 템플릿 프로젝트입니다.  
JWT + Redis를 활용한 일반 로그인 기능과 Swagger를 통한 API 테스트를 지원합니다.

## 🛠 기술 스택

- Java 21
- Spring Boot 3.5.5
- Spring Security
- Spring Data JPA
- Spring Data Redis
- JWT (io.jsonwebtoken)
- MySQL / H2 (개발용)
- Swagger/OpenAPI (`springdoc-openapi-starter-webmvc-ui`)

## 🚀 시작하기

### 사전 요구사항

- Java 21
- Gradle 8.7 or higher
- MySQL
- Redis

### Redis 서버 실행

프로젝트를 실행하기 전에 Redis 서버가 실행 중이어야 합니다. 아래는 각 운영체제별 Redis 설치 및 실행 방법입니다.

- **macOS (using Homebrew):**
  ```bash
  # Redis 설치
  brew install redis
  # Redis 서버 실행 (백그라운드)
  brew services start redis
  ```
- **Linux (using apt - Debian/Ubuntu):**
  ```bash
  # Redis 설치
  sudo apt-get update
  sudo apt-get install redis-server
  # Redis 서버 실행
  sudo systemctl start redis-server
  ```
- **Windows:**
  Windows에서는 [WSL2(Windows Subsystem for Linux)](https://learn.microsoft.com/ko-kr/windows/wsl/install)를 사용하여 Linux 환경에 Redis를 설치하는 것을 권장합니다.
- **기타:** 자세한 내용은 [공식 Redis 설치 문서](https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/)를 참고하세요.

### 실행 방법

1.  **프로젝트 클론:**
    ```bash
    git clone https://github.com/your-username/usertemplate.git
    cd usertemplate
    ```

2.  **application-dev.properties 파일 생성:**
    `src/main/resources/` 경로에 `application-dev.properties` 파일을 생성하고 아래 내용을 채워주세요. 이 파일은 `.gitignore`에 의해 관리되지 않으므로 민감한 정보를 안전하게 보관할 수 있습니다.

    ```properties
    # Database
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    spring.jpa.hibernate.ddl-auto=update

    # Redis
    spring.data.redis.host=localhost
    spring.data.redis.port=6379

    # JWT
    jwt.secret=your-super-strong-jwt-secret-key-that-is-at-least-256-bits-long
    jwt.expiration=86400000 # Access Token 만료 시간 (ms단위, 기본값: 1일)
    jwt.refresh-expiration=604800000 # Refresh Token 만료 시간 (ms단위, 기본값: 7일)
    ```

3.  **애플리케이션 실행:**
    프로젝트 루트 디렉토리에서 아래 명령어를 실행합니다.

    ```bash
    ./gradlew bootRun
    ```

4.  **API 문서 확인:**
    애플리케이션 실행 후, 아래 주소로 접속하여 API 문서를 확인할 수 있습니다.
    [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## ⚙️ 설정

주요 설정은 `src/main/resources/application.properties`와 `src/main/resources/application-dev.properties`에서 관리됩니다.

-   `application.properties`: 공통 설정을 관리합니다. 현재 `dev` 프로파일이 활성화되어 있습니다.
    ```properties
    spring.profiles.active=dev
    server.port=8080
    ```
-   `application-dev.properties`: 개발 환경의 민감한 정보(DB, Redis, JWT Secret)를 관리합니다. (위의 "실행 방법" 섹션 참고)
-   `SecurityConfig.java`: Spring Security 관련 설정을 담당하며, URL 접근 권한 등을 설정할 수 있습니다.
-   `OpenApiConfig.java`: Swagger(OpenAPI) 관련 설정을 담당합니다.

## 💾 Redis 설정

이 프로젝트는 JWT의 Refresh Token을 저장하고 관리하기 위해 Redis를 사용합니다.

### 주요 역할

-   **Refresh Token 저장소**: 사용자가 로그인하면, 서버는 Access Token과 함께 Refresh Token을 발급합니다. 이 Refresh Token은 사용자의 이메일을 Key로 하여 Redis에 14일의 유효기간으로 저장됩니다. Access Token이 만료되었을 때, 이 Refresh Token을 사용하여 새로운 Access Token을 발급받을 수 있습니다.

### 관련 파일

-   `global/redis/RedisConfig.java`: Redis 서버와의 연결을 설정하고 `RedisTemplate`을 Bean으로 등록합니다. `application-dev.properties`에 정의된 `host`와 `port` 정보를 사용합니다.
-   `global/redis/RedisProperties.java`: `application-dev.properties` 파일에서 Redis 연결 정보(host, port)를 읽어오는 컴포넌트입니다.
-   `global/redis/RedisUtil.java`: `RedisTemplate`을 사용하여 Redis에 데이터를 쉽게 저장하고 만료 시간을 설정하는 유틸리티 클래스입니다. `AuthServiceImpl`에서 Refresh Token을 저장할 때 이 클래스를 사용합니다.
-   `auth/service/AuthServiceImpl.java`: `login` 메소드에서 로그인 성공 시 `RedisUtil`을 호출하여 Refresh Token을 Redis에 저장합니다.

### 설정 방법

`src/main/resources/application-dev.properties` 파일에 아래와 같이 Redis 서버 정보를 입력해야 합니다.

```properties
# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

## 📦 프로젝트 구조

```
.
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
```
