-- PostgreSQL Schema Migration Script for Campus Resource & Peer Hub
-- Database: campushubdb

-- Drop tables if they already exist (for clean initialization)
DROP TABLE IF EXISTS resource_bookmarks CASCADE;
DROP TABLE IF EXISTS resource_upvotes CASCADE;
DROP TABLE IF EXISTS study_groups CASCADE;
DROP TABLE IF EXISTS resources CASCADE;
DROP TABLE IF EXISTS users CASCADE;

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

-- Index for faster authentication email lookup
CREATE INDEX idx_users_email ON users(email);

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

-- Indexes for filtered resource search & uploader queries
CREATE INDEX idx_resources_search ON resources(subject_code, semester, category);
CREATE INDEX idx_resources_uploader ON resources(uploader_id);

-- 3. STUDY GROUPS TABLE
CREATE TABLE study_groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL,
    subject VARCHAR(50) NOT NULL,
    created_by INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    max_members INT DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for group lookup by creator and subject
CREATE INDEX idx_study_groups_created_by ON study_groups(created_by);
CREATE INDEX idx_study_groups_subject ON study_groups(subject);

-- 4. RESOURCE UPVOTES TABLE
CREATE TABLE resource_upvotes (
    upvote_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    resource_id INT NOT NULL REFERENCES resources(resource_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_resource_upvote UNIQUE (user_id, resource_id)
);

-- 5. RESOURCE BOOKMARKS TABLE
CREATE TABLE resource_bookmarks (
    bookmark_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    resource_id INT NOT NULL REFERENCES resources(resource_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_resource_bookmark UNIQUE (user_id, resource_id)
);
