# School Management System - Backend (Spring Boot 3)

Backend RESTful API service for the School Management System built with Spring Boot 3, Spring Data JPA, PostgreSQL, Spring Security 6 with JWT, and Docker.

---

## 🚀 Features

- **Authentication & Authorization**:
  - Role-based Access Control (`ROLE_ADMIN`, `ROLE_TEACHER`, `ROLE_STUDENT`)
  - Stateless JWT token authentication
  - User self-registration with automatic student profile provisioning
- **Academic Management**:
  - **Departments**: Manage academic faculties & departments
  - **Teachers**: Manage instructors with detailed teacher profiles
  - **Courses**: Manage course offerings, credit hours, seat capacities, and teacher assignments
  - **Students**: Student records, demographic info, and enrollment tracking
  - **Enrollments**: Transactional course enrollment, automatic seat inventory deduction, discount percentage calculation, receipt generation, and dropping/canceling courses with automatic seat restocking
- **Architecture**:
  - Layered Clean Architecture (Controller -> Service -> Repository -> Entity)
  - Data Transfer Objects (DTOs) and ModelMapper
  - JPA Specifications for dynamic filtering, searching, and pagination
  - Global Exception Handling with structured API responses

---

## 🛠️ Tech Stack

- **Java**: 21 (Eclipse Temurin)
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **Security**: Spring Security & JWT (jjwt 0.12.6)
- **Build Tool**: Maven (`mvnw`)
- **Containerization**: Docker & Docker Compose
- **API Documentation**: OpenAPI 3 / Swagger (`/swagger-ui.html`)

---

## 🔐 Security Architecture & Request Flow

The backend uses **stateless token-based authentication** powered by Spring Security 6 and JSON Web Tokens (JWT). The server does not store user sessions in memory; instead, every client request carries a digitally signed cryptographic token.

### 1. High-Level Request Pipeline

```mermaid
flowchart TD
    Client["Client (React Web / Flutter Mobile)"]
    
    subgraph SpringSecurity ["Spring Security 6 (Stateless Layer)"]
        Cors["1. CorsFilter (CORS Header Check)"]
        JwtFilter["2. JwtAuthenticationFilter (Extract & Verify)"]
        JwtProvider["JwtTokenProvider (HMAC-SHA256 & Claims)"]
        SecCtx["SecurityContextHolder (Principal & Authorities)"]
        AuthZ["3. AuthorizationFilter (Role Verification)"]
    end
    
    subgraph SpringMVC ["Spring Boot Application Layers"]
        Dispatcher["DispatcherServlet"]
        Controller["REST Controller (@RestController)"]
        Service["Service Layer (@Service - Business Logic)"]
        Repo["Data Access Layer (@Repository - JPA / Hibernate)"]
    end
    
    subgraph Database ["Persistence Layer"]
        Postgres[("PostgreSQL Database")]
    end
    
    Client -->|"HTTP Request + Header: Authorization: Bearer <token>"| Cors
    Cors --> JwtFilter
    JwtFilter <-->|"Validate Signature & Claims"| JwtProvider
    JwtFilter -->|"Set Authentication"| SecCtx
    JwtFilter --> AuthZ
    AuthZ -->|"Authorized"| Dispatcher
    AuthZ -.->|"Unauthorized (401) / Forbidden (403)"| Client
    Dispatcher --> Controller
    Controller --> Service
    Service --> Repo
    Repo <--> Postgres
    Controller -->|"Structured JSON Response"| Client
```

---

### 2. User Authentication Flow (Login & Token Issuance)

When a user signs in, credentials are submitted, verified with BCrypt against the PostgreSQL database, and a cryptographically signed JWT is returned to the client:

```mermaid
sequenceDiagram
    autonumber
    actor Client as Client (Web / Mobile)
    participant AuthCtrl as AuthController (/api/v1/auth/login)
    participant AuthSvc as AuthServiceImpl
    participant AuthMgr as AuthenticationManager
    participant DaoProv as DaoAuthenticationProvider
    participant UserDetails as CustomUserDetailsService
    participant DB as PostgreSQL Database
    participant JwtProv as JwtTokenProvider

    Client->>AuthCtrl: POST /api/v1/auth/login { usernameOrEmail, password }
    AuthCtrl->>AuthSvc: login(loginDto)
    AuthSvc->>AuthMgr: authenticate(UsernamePasswordAuthenticationToken)
    AuthMgr->>DaoProv: authenticate(authentication)
    DaoProv->>UserDetails: loadUserByUsername(usernameOrEmail)
    UserDetails->>DB: SELECT user JOIN roles WHERE username=? OR email=?
    DB-->>UserDetails: User Entity (id, username, email, passwordHash, roles)
    UserDetails-->>DaoProv: UserDetails (Principal with GrantedAuthorities)
    DaoProv->>DaoProv: BCrypt.checkpw(rawPassword, passwordHash)
    
    alt Password Mismatch / User Not Found
        DaoProv-->>Client: 401 Unauthorized / BadCredentialsException
    else Credentials Match
        DaoProv-->>AuthMgr: Authentication (Authenticated = true)
        AuthMgr-->>AuthSvc: Authentication object
        AuthSvc->>JwtProv: generateToken(authentication)
        Note right of JwtProv: Assembles JWT Token:<br/>- Header: { alg: HS256, typ: JWT }<br/>- Payload: { sub: username, roles: [...], exp: timestamp }<br/>- Signature: HMAC-SHA256(header.payload, secretKey)
        JwtProv-->>AuthSvc: Signed JWT String
        AuthSvc-->>AuthCtrl: AuthResponse(accessToken, tokenType="Bearer")
        AuthCtrl-->>Client: 200 OK + { accessToken: "eyJhbGciOi...", tokenType: "Bearer" }
        Note over Client: Stores accessToken in SharedPreferences (Mobile) or localStorage (Web)
    end
```

---

### 3. Authenticated Request Flow (Accessing Protected Endpoints)

For subsequent requests (e.g. creating courses, enrolling students, managing departments), the client sends the token in the `Authorization` header:

```mermaid
sequenceDiagram
    autonumber
    actor Client as Client (Web / Mobile)
    participant FilterChain as Spring Security Filter Chain
    participant JwtFilter as JwtAuthenticationFilter
    participant JwtProv as JwtTokenProvider
    participant UserDetails as CustomUserDetailsService
    participant SecContext as SecurityContextHolder
    participant Dispatcher as DispatcherServlet
    participant Controller as REST Controller (e.g. EnrollmentController)
    participant Service as Service Layer (e.g. EnrollmentServiceImpl)
    participant Repo as JPA Repository
    participant DB as PostgreSQL Database

    Client->>FilterChain: HTTP Request (e.g. POST /api/v1/enrollments)<br/>Header: Authorization: Bearer <jwt_token>
    FilterChain->>JwtFilter: doFilterInternal(request, response, filterChain)
    JwtFilter->>JwtFilter: getJwtFromRequest() -> extracts "eyJhbGci..."
    
    alt Token Missing or Expired/Invalid
        JwtFilter-->>Client: 401 Unauthorized (via JwtAuthenticationEntryPoint)
    else Token Signature & Expiry Valid
        JwtFilter->>JwtProv: validateToken(jwt)
        JwtProv-->>JwtFilter: true
        JwtFilter->>JwtProv: getUsernameFromJwt(jwt)
        JwtProv-->>JwtFilter: username
        JwtFilter->>UserDetails: loadUserByUsername(username)
        UserDetails-->>JwtFilter: UserDetails (Roles: [ROLE_STUDENT, ...])
        JwtFilter->>SecContext: setAuthentication(UsernamePasswordAuthenticationToken)
        JwtFilter->>FilterChain: filterChain.doFilter(request, response)
        
        alt Insufficient Role Permissions (e.g. STUDENT accessing ADMIN route)
            FilterChain-->>Client: 403 Forbidden (via JwtAccessDeniedHandler)
        else Role Authorized
            FilterChain->>Dispatcher: Forward to DispatcherServlet
            Dispatcher->>Controller: Route to target @PostMapping / @GetMapping method
            Controller->>Service: Execute business logic (e.g. seat check, discount calculation)
            Service->>Repo: Data access operations (save / find)
            Repo->>DB: Execute SQL Queries (INSERT, UPDATE, SELECT)
            DB-->>Repo: SQL Results
            Repo-->>Service: Managed Entity
            Service-->>Controller: Response DTO
            Controller-->>Client: 200 OK / 201 Created (JSON Response)
        end
    end
```

---

### 4. ដំណើរការសង្ខេបជាភាសាខ្មែរ (Workflow Summary in Khmer)

1. **ការ Login (Authentication)**:
   - Client (Web/Mobile) ផ្ញើ Username/Email & Password ទៅកាន់ `/api/v1/auth/login`។
   - `AuthenticationManager` ផ្ទៀងផ្ទាត់ Password ជាមួយ Database ដោយប្រើ `BCrypt`។
   - បើត្រឹមត្រូវ `JwtTokenProvider` នឹងបង្កើត **JWT Access Token** ដែលមានបង្កប់ Username, Roles និង Expiration រួចចុះហត្ថលេខាឌីជីថល (HMAC-SHA256) ផ្ញើទៅឱ្យ Client រក្សាទុក។

2. **ការហៅ Request ធម្មតា (Authenticated Client Request)**:
   - រាល់ពេល Client ត្រូវការទិន្នន័យ (ដូចជា មើលសិស្ស, ចុះឈ្មោះរៀន Course), Client ត្រូវភ្ជាប់ Header `Authorization: Bearer <token>`។
   - **`JwtAuthenticationFilter`** ស្ទាក់ចាប់ Request មុនគេ រួចពិនិត្យមើល Token៖
     - បើគ្មាន Token ឬ Token ផុតកំណត់ -> ឆ្លើយតប **401 Unauthorized** ភ្លាមៗ (តាមរយៈ `JwtAuthenticationEntryPoint`)។
     - បើ Token ត្រឹមត្រូវ -> ស្វែងរក Roles របស់ User រួចបញ្ជូនទៅដាក់ក្នុង `SecurityContextHolder`។
   - ប្រសិនបើ Role មិនត្រូវនឹងលក្ខខណ្ឌកំណត់ (ឧទាហរណ៍៖ Student ចង់លុបគ្រូ) -> ឆ្លើយតប **403 Forbidden** (តាមរយៈ `JwtAccessDeniedHandler`)។
   - ប្រសិនបើមានសិទ្ធិគ្រប់គ្រាន់ -> Request ធ្វើដំណើរឆ្លងកាត់ **`DispatcherServlet` -> `Controller` -> `Service` -> `Repository` -> `PostgreSQL`** រួចត្រឡប់ទិន្នន័យជា JSON ទៅកាន់ Client វិញដោយជោគជ័យ។

---

## ⚙️ Getting Started

### 1. Prerequisites
- Java 21+ installed
- PostgreSQL 15+ installed or running via Docker

### 2. Environment Configuration
Copy the template `.env.example` file to `.env`:
```bash
cp .env.example .env
```
Update your database credentials in `.env`:
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/school_management_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password_here
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SERVER_PORT=8080
```

### 3. Run Locally (Maven)
```bash
./mvnw clean spring-boot:run
```

### 4. Run with Docker Compose
```bash
docker compose up --build -d
```

---

## 🔒 Security & Sensitive Data
- Database credentials and secrets are managed via environment variables and excluded from version control (`.gitignore`).
- Never commit `.env` or production credentials.
