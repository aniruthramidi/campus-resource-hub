# 🎓 Cloud-Hosted Campus Resource & Peer Hub

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Google%20Cloud%20SQL-blue.svg)](https://www.postgresql.org/)
[![Google Cloud Storage](https://img.shields.io/badge/GCP-Cloud%20Storage%20SDK-red.svg)](https://cloud.google.com/storage)
[![JWT Security](https://img.shields.io/badge/Security-Spring%20Security%20%2B%20JWT-purple.svg)](https://jwt.io/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

A centralized, secure, and scalable web portal for university students to upload, search, download, and organize academic resources (Previous Year Questions - PYQs, lecture notes, lab manuals) and discover peer study groups.

---

## 📌 Features

- 🔍 **Instant Search & Filter**: Search 150+ academic resources by subject code (e.g. `CS301`), title, keyword, semester (1 to 8), or resource category (`PYQ`, `NOTES`, `LAB_MANUAL`).
- ☁️ **Cloud Object Storage (GCP GCS)**: Secure document handling with UUID file name sanitization and direct GCS URL storage (restricting uploads strictly to valid PDFs and images).
- 🛡️ **JWT Security & Auth**: Role-based authentication (`STUDENT`, `FACULTY`, `ADMIN`) using Spring Security and stateless JSON Web Tokens.
- 👥 **Peer Study Groups**: Discover, create, and join subject-specific peer study groups with dynamic member capacity limits.
- 📱 **Responsive Monochrome SPA**: Modern, high-contrast monochrome Single Page Application interface with glassmorphism cards and toast notifications.

---

## 🏗️ Project Architecture & Tech Stack

### Core Technologies
- **Backend Framework**: Java 17+, Spring Boot 3.2.5 (Spring Data JPA, Spring Security, Spring Validation)
- **Database**: PostgreSQL (Google Cloud SQL in Production)
- **Cloud Storage**: Google Cloud Storage Java SDK (`com.google.cloud:google-cloud-storage:2.38.0`)
- **Authentication**: JJWT (`io.jsonwebtoken:jjwt-api:0.12.5`)
- **Frontend**: HTML5, Modern Vanilla CSS (Obsidian/Monochrome theme), Vanilla JavaScript (Single Page Architecture)

---

## 🗄️ Relational Database Schema

The database schema is defined in [`schema.sql`](campus-hub/src/main/resources/schema.sql):

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

## 📁 Repository Structure

```text
campus-resource-hub/
├── .gitignore
├── README.md
└── campus-hub/
    ├── pom.xml                                  # Maven Dependencies Configuration
    └── src/
        └── main/
            ├── java/
            │   └── com/
            │       └── campushub/
            │           ├── CampusHubApplication.java # Spring Boot Application Main
            │           ├── config/              # Security & GCP Storage Beans
            │           ├── controller/          # REST API Controllers
            │           ├── dto/                 # Request & Response DTOs
            │           ├── entity/              # JPA Entities (User, Resource, StudyGroup)
            │           ├── exception/           # Global Exception Handler (@ControllerAdvice)
            │           ├── repository/          # Spring Data JPA Repositories
            │           ├── security/            # JWT Token Provider & Filter
            │           └── service/             # Business Logic & GCS Upload Service
            └── resources/
                ├── application.yml              # Spring Boot Application Properties
                ├── schema.sql                   # PostgreSQL Migration DDL
                └── static/                      # Single Page Application Frontend
                    ├── app.js                   # Client-side SPA Logic & Dataset
                    ├── hello.txt                # Sample Test File
                    ├── index.html               # Main HTML Layout
                    └── styles.css               # Monochrome Glassmorphism CSS
```

---

## 🚀 Local Development Setup

### Prerequisites
- **Java 17** or higher
- **Maven 3.8+**
- **PostgreSQL 14+** (running on `localhost:5432` with database `campushubdb`)

### 1. Database Setup
Create the PostgreSQL database and run the schema migration script:
```bash
createdb -U postgres campushubdb
psql -U postgres -d campushubdb -f campus-hub/src/main/resources/schema.sql
```

### 2. Configure Environment Variables
Set database credentials and GCP storage settings in `campus-hub/src/main/resources/application.yml` or export environment variables:
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/campushubdb
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export GCP_STORAGE_BUCKET=campus-hub-resources
export JWT_SECRET=your_secure_256bit_secret_key_here
```

### 3. Build & Run the Spring Boot Backend
Navigate to `campus-hub/` and start the Spring Boot server:
```bash
cd campus-hub
mvn clean spring-boot:run
```

The application will start at:
👉 **`http://localhost:8080`**

---

## 📡 REST API Summary

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new student account | ❌ No |
| `POST` | `/api/auth/login` | Login & acquire JWT access token | ❌ No |
| `GET` | `/api/resources` | Search & filter resources by semester/category | ❌ No |
| `POST` | `/api/resources/upload` | Upload resource & push file to GCS bucket | 🔒 Yes (JWT) |
| `PUT` | `/api/resources/{id}/upvote` | Increment upvotes for a resource | 🔒 Yes (JWT) |
| `GET` | `/api/groups` | Discover active peer study groups | ❌ No |
| `POST` | `/api/groups` | Create a new peer study group | 🔒 Yes (JWT) |
| `POST` | `/api/groups/{id}/join` | Join an existing study group | 🔒 Yes (JWT) |

---

## 📄 License
Distributed under the **MIT License**. See `LICENSE` for details.
