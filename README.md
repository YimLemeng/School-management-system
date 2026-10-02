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
