# 📚 LMS Microservice Project

> A state-of-the-art **Learning Management System** built with a **Spring Boot Microservices Architecture** and a **Next.js 15+ Frontend**. This project demonstrates enterprise-level software engineering practices, including Design Patterns, Clean Code, and Formal Verification (OCL).

---

## 📖 Table of Contents

1. [🚀 Project Overview](#-project-overview)
2. [🏛️ Architecture & Microservices](#️-architecture--microservices)
3. [🎨 Frontend Excellence](#-frontend-excellence)
4. [🧩 Design Patterns & Clean Code](#-design-patterns--clean-code)
5. [📐 Documentation & Diagrams](#-documentation--diagrams)
6. [🔌 API Reference](#-api-reference)
7. [🧰 Tech Stack](#-tech-stack)
8. [🚦 Getting Started](#-getting-started)
9. [🗂️ Port Reference](#-port-reference)

---

## 🚀 Project Overview

This LMS (Learning Management System) is an enterprise-grade platform designed for scalability and maintainability. It allows users to register as **Students** or **Teachers**, manage courses, upload content, and handle enrollments through a secure, decoupled architecture.

**Key Features:**
- **Role-Based Access Control (RBAC):** Granular security for Students, Teachers, and Admins.
- **Asynchronous Processing:** Efficient handling of file uploads and data processing.
- **Distributed State:** Token blacklisting via Redis for secure stateless authentication.
- **Scalable Content:** Dedicated upload service with volume mapping for media assets.

---

## 🏛️ Architecture & Microservices

The system is built on a **Microservices Architecture**, where each domain is a self-contained unit with its own database, ensuring high availability and independent scalability.

### 🗺️ Service Map
- **🔍 Eureka Server (`:8761`)**: The heartbeat of the system. Handles service discovery and registration.
- **🚪 API Gateway (`:8080`)**: The intelligent entry point. Handles routing, security filtering, and request logging.
- **🔐 Auth Service (`:8081`)**: Identity management. Issues JWTs, manages sessions, and handles user registration.
- **👤 User Service (`:8082`)**: Profile management. Maintains detailed user data and role-specific information.
- **📘 Course Service (`:8083`)**: The core engine. Manages courses, lessons, and the enrollment lifecycle.
- **📁 Upload Service (`:8084`)**: Media handling. Manages file uploads (videos, PDFs) and content storage.

---

## 🎨 Frontend Excellence

Located in [`/LMSFrontend`](file:///d:/LMS/Backend/LMS_MicroService/LMSFrontend), the frontend is a modern, high-performance web application.

- **Framework:** Next.js 15 (App Router) for Server-Side Rendering (SSR) and SEO optimization.
- **Styling:** Tailwind CSS 4.0 for utility-first responsive design.
- **UI Components:** Built on Radix UI and Shadcn UI for accessible, premium-feel components.
- **State Management:** React Hooks and Context API for fluid user interactions.
- **Features:**
    - Dynamic Dashboard based on User Role.
    - Interactive Course Creation wizard for Teachers.
    - Seamless Video/PDF learning experience for Students.

---

## 🧩 Design Patterns & Clean Code

This project is a showcase of **Clean Code** and **SOLID** principles.

### 🛠️ Design Patterns Implemented
1.  **Strategy Pattern:** Used for dynamic Authorization. Different strategies (e.g., `RoleStrategy`, `OwnerStrategy`) are injected into the `AuthorizationAspect` to handle security logic without "if-else" hell.
2.  **Aspect-Oriented Programming (AOP):** Decouples cross-cutting concerns like logging and security from business logic using custom annotations like `@RequireRole`.
3.  **Repository Pattern:** Abstracts data access logic, making it easier to switch persistence layers.
4.  **Data Transfer Object (DTO):** Ensures strict API contracts and prevents sensitive internal entities from leaking to the client.
5.  **API Gateway Pattern:** Centralizes cross-cutting concerns (authentication, routing) at the entry point.

### ✨ Clean Code Practices
- **Layered Architecture:** Clear separation between Controllers, Services, and Repositories.
- **Single Responsibility Principle:** Each service and class has one clear purpose.
- **Lombok:** Eliminates boilerplate code (Getters, Setters, Constructors).
- **Custom Exception Handling:** Centralized `@ControllerAdvice` for consistent API error responses.

---

## 📐 Documentation & Diagrams

We believe in "Code as Documentation." All business rules are formally specified using **OCL (Object Constraint Language)**.

### 🖼️ System Diagrams
| Category | Diagram Link |
|---|---|
| **ERD** | [Database Schema](file:///d:/LMS/Backend/LMS_MicroService/documentation/ERD/ERD.png) |
| **System Architecture** | [Bounded Contexts](file:///d:/LMS/Backend/LMS_MicroService/documentation/Class/1_bounded_context.png) |
| **User Flow** | [Registration Sequence](file:///d:/LMS/Backend/LMS_MicroService/documentation/Sequence/Registeration_Seq.png) |
| **Course Lifecycle** | [Enrollment Sequence](file:///d:/LMS/Backend/LMS_MicroService/documentation/Sequence/enrollment_Seq.png) |
| **Content Management** | [Lesson Upload Sequence](file:///d:/LMS/Backend/LMS_MicroService/documentation/Sequence/Upload_Lesson_seq.png) |

### 📜 Formal Rules (OCL)
Business constraints (e.g., *"Students cannot enroll in their own courses"*) are formally documented in [`documentation/constraints.ocl`](file:///d:/LMS/Backend/LMS_MicroService/documentation/constraints.ocl).

---

## 🔌 API Reference

Full API documentation, including request/response examples and `curl` commands, is available in the dedicated reference file:

👉 **[View Detailed API Reference](file:///d:/LMS/Backend/LMS_MicroService/API_REFERENCE.md)**

---

## 🧰 Tech Stack

| Component | Technology |
|---|---|
| **Backend** | Java 17, Spring Boot 3.x, Spring Cloud |
| **Frontend** | Next.js 15, React 19, TypeScript |
| **Databases** | PostgreSQL 15 (Relational), Redis (Cache/Blacklist) |
| **Security** | Spring Security, JWT (JJWT) |
| **DevOps** | Docker, Docker Compose |
| **Design** | Tailwind CSS, Shadcn UI |

---

## 🚦 Getting Started

### 🐳 The One-Command Start
The easiest way to run the entire stack (Backend + Infrastructure) is via Docker Compose:

```powershell
# Build and start all services
docker-compose up --build
```

### 💻 Local Frontend Development
```bash
cd LMSFrontend
npm install
npm run dev
```

---

## 🗂️ Port Reference

| Service | Port | External Access |
|---|---|---|
| **API Gateway** | 8080 | ✅ `http://localhost:8080` |
| **Eureka Server** | 8761 | ✅ `http://localhost:8761` |
| **Frontend** | 3000 | ✅ `http://localhost:3000` |
| **Auth Service** | 8081 | ❌ Internal Only |
| **User Service** | 8082 | ❌ Internal Only |
| **Course Service** | 8083 | ❌ Internal Only |
| **Upload Service** | 8084 | ❌ Internal Only |
: The original PowerShell setup scripts injected a hidden UTF-8 BOM encoding that caused Java compiler errors (`illegal character: '\ufeff'`). It also improperly escaped strings, turning `@Value("${variable}")` into `@Value("\")`. These have been stripped and corrected across all `pom.xml` and `.java` files.
2. **Missing Security Dependencies**: `user-service`, `course-service`, and `upload-service` contained `SecurityConfig.java` files, but their `pom.xml` files were missing the `spring-boot-starter-security` dependency, causing "package does not exist" errors.
3. **Missing DTOs**: The `api-gateway` failed to compile because it relied on `ValidateResponse.java` to communicate with the `auth-service`, but the file was missing from the gateway's `dto` package.
4. **Java Lambda Compilation Errors**: In `course-service` (`AuthorizationAspect.java`), a local variable modified inside a `for` loop was used inside a lambda `orElseThrow()`. Java requires variables used in lambdas to be `final` or effectively final. This was fixed by assigning it to a new `final` variable before the lambda.
