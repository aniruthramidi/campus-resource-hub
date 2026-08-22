# 🎓 Cloud-Hosted Campus Resource & Peer Hub

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Google%20Cloud%20SQL-blue.svg)](https://www.postgresql.org/)
[![Google Cloud Storage](https://img.shields.io/badge/GCP-Cloud%20Storage%20SDK-red.svg)](https://cloud.google.com/storage)
[![Swagger UI](https://img.shields.io/badge/OpenAPI-Swagger%20v3.0-green.svg)](http://localhost:8080/swagger-ui.html)
[![JWT Security](https://img.shields.io/badge/Security-Spring%20Security%20%2B%20JWT-purple.svg)](https://jwt.io/)
[![CI Build](https://img.shields.io/badge/CI-GitHub%20Actions-blue.svg)](.github/workflows/ci.yml)

A centralized, secure, and scalable web portal for university students to upload, search, download, and organize academic resources (Previous Year Questions - PYQs, lecture notes, lab manuals) and discover peer study groups.

---

## 📌 Features

- 🔍 **Instant Search & Filter**: Search 150+ academic resources by subject code (e.g. `CS301`), title, keyword, semester (1 to 8), or resource category (`PYQ`, `NOTES`, `LAB_MANUAL`).
- ⚡ **Trending & Bookmarks**: View top upvoted trending resources and bookmark study materials to your personal saved library.
- 👤 **User Profile Management**: Retrieve and update profile details (department, semester, full name) via protected REST endpoints.
- 👥 **Peer Study Groups**: Discover, form, join, and leave subject-specific peer study groups with dynamic capacity bounds.
- 📖 **Interactive OpenAPI / Swagger UI**: Built-in Swagger documentation available at `/swagger-ui.html` for easy API testing and schema exploration.
- ☁️ **Cloud Object Storage (GCP GCS)**: Secure document handling with UUID file name sanitization and direct GCS URL storage.
- 🛡️ **JWT Security & Auth**: Role-based authentication (`STUDENT`, `FACULTY`, `ADMIN`) using Spring Security and stateless JSON Web Tokens.
- 🌗 **Dark/Light Theme Toggle & Toasts**: Dynamic monochrome UI theme switcher and animated toast notifications.

---

## 🏗️ Project Architecture & Tech Stack

### Core Technologies
- **Backend Framework**: Java 17+, Spring Boot 3.2.5 (Spring Data JPA, Spring Security, Spring Validation)
- **API Documentation**: SpringDoc OpenAPI 2.5.0 / Swagger UI
- **Database**: PostgreSQL (Google Cloud SQL in Production) / H2 In-Memory (Test Profile)
- **Cloud Storage**: Google Cloud Storage Java SDK (`com.google.cloud:google-cloud-storage:2.38.0`)
- **Authentication**: JJWT (`io.jsonwebtoken:jjwt-api:0.12.5`)
- **Frontend**: HTML5, Modern Vanilla CSS (Obsidian/Monochrome theme with theme switch), Vanilla JavaScript
- **CI/CD**: GitHub Actions pipeline for automated Maven compilation, testing, and frontend verification

---

## 🌐 Key API Endpoints Matrix

| HTTP Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register a new student account | No |
| `POST` | `/api/auth/login` | Authenticate and obtain JWT token | No |
| `GET` | `/api/auth/me` | Fetch current user basic info | Yes |
| `GET` | `/api/auth/profile` | Retrieve detailed user profile | Yes |
| `PUT` | `/api/auth/profile` | Update profile (department, semester, name) | Yes |
| `GET` | `/api/resources` | Search resources (query, semester, category) | No |
| `GET` | `/api/resources/trending` | Get top 10 upvoted trending resources | No |
| `POST` | `/api/resources` | Upload new resource to GCP Cloud Storage | Yes |
| `POST` | `/api/resources/{id}/upvote` | Toggle upvote on resource | Yes |
| `POST` | `/api/resources/{id}/bookmark` | Toggle bookmark on resource | Yes |
| `GET` | `/api/resources/bookmarks` | Fetch user's bookmarked resources | Yes |
| `GET` | `/api/groups` | List peer study groups | No |
| `POST` | `/api/groups` | Create new peer study group | Yes |
| `POST` | `/api/groups/{id}/join` | Join peer study group | Yes |
| `POST` | `/api/groups/{id}/leave` | Leave peer study group | Yes |

---

## 🗄️ Database Schema

The relational schema is configured in [`schema.sql`](campus-hub/src/main/resources/schema.sql):

```sql
-- 1. USERS TABLE
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    department VARCHAR(50),
    semester INT,
    role VARCHAR(20) DEFAULT 'STUDENT' CHECK (role IN ('STUDENT', 'ADMIN', 'FACULTY')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. RESOURCES TABLE
CREATE TABLE resources (
    resource_id SERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    subject_code VARCHAR(20) NOT NULL,
    semester INT NOT NULL,
    category VARCHAR(30) NOT NULL CHECK (category IN ('PYQ', 'NOTES', 'LAB_MANUAL')),
    file_gcs_url VARCHAR(500) NOT NULL,
    uploader_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    upvotes INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. STUDY GROUPS TABLE
CREATE TABLE study_groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL,
    subject VARCHAR(50) NOT NULL,
    created_by INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    max_members INT DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🚀 Local Development & Testing

### Running Spring Boot Backend

```bash
cd campus-hub
mvn spring-boot:run
```

### Running Unit & Integration Tests (H2 Database Profile)

```bash
cd campus-hub
mvn clean test -Dspring.profiles.active=test
```

### Accessing Swagger API Documentation

Once the application is running on port `8080`, navigate to:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON Spec**: `http://localhost:8080/v3/api-docs`

---

## 📜 Changelog Overview

- **`feat(api-docs)`**: Added SpringDoc OpenAPI Swagger UI documentation endpoints.
- **`feat(backend)`**: User profile management (`UserProfileDto`), study group membership operations, and trending resources queries.
- **`test(backend)`**: Added Spring Boot unit & integration tests for JWT security and authentication.
- **`feat(frontend)`**: Integrated Dark/Light mode theme switch, Toast alerts, and search/trending pills.
- **`ci`**: Automated GitHub Actions workflow for build, test, and frontend assets.
