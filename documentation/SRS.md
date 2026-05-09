# Software Requirements Specification (SRS)
## Project: Learning Management System (LMS) Microservices

**Version:** 1.0  
**Date:** 2026-05-09  
**Status:** Final

---


### 1.4 Project Scope
The LMS is an enterprise-grade education platform that facilitates online learning. It allows Teachers to create and manage courses, Students to enroll and consume content, and Admins to oversee the system. The platform is built using a decoupled microservices architecture to ensure scalability, resilience, and maintainability.

---

## 2. Overall Description

### 2.1 Product Perspective
The LMS is a distributed system composed of several independent microservices communicating via an API Gateway. It uses a Next.js frontend for user interaction and a Spring Boot backend for business logic.

**Architectural Components:**
- **Identity Provider:** Auth Service (JWT based).
- **Service Discovery:** Eureka Server.
- **Gateway:** Spring Cloud Gateway.
- **Persistence:** PostgreSQL for relational data, Redis for caching/blacklisting.

### 2.2 Product Functions
- User registration and role-based login.
- Profile management.
- Course creation, modification, and deletion (Teachers/Admins).
- Lesson management and media attachment.
- Enrollment request and approval workflow.
- File upload and secure storage.

### 2.3 User Classes and Characteristics
| User Class | Description |
|---|---|
| **Student** | Can browse courses, enroll in courses (subject to approval), and view lesson content. |
| **Teacher** | Can create courses, add lessons, upload media, and manage student enrollments for their courses. |
| **Admin** | Has global access to manage all users, courses, and system settings. |

### 2.4 Operating Environment
- **Backend:** Java 17+, Spring Boot 3.x.
- **Frontend:** Next.js 15+, React 19.
- **Deployment:** Docker & Docker Compose.
- **Database:** PostgreSQL 15, Redis 7.

### 2.5 Design and Implementation Constraints
- **Security:** Must use JWT for stateless authentication.
- **Design Patterns:** Must implement Strategy Pattern for authorization and AOP for cross-cutting concerns.
- **Formal Verification:** Business rules must adhere to OCL (Object Constraint Language) specifications.

---

## 3. System Features

### 3.1 Authentication & Authorization (Auth Service)
- **ID: FR-01**
- **Description:** Users must be able to register, login, and obtain a JWT.
- **Requirements:**
    - Support for `STUDENT` and `TEACHER` roles during registration.
    - Token issuance upon successful login.
    - Token blacklisting on logout using Redis.
    - Token refreshing mechanism.
- **Endpoints:**
    - `POST /auth/register`: Register a new user.
    - `POST /auth/login`: Authenticate user and return JWT.
    - `POST /auth/refresh`: Refresh an expired JWT.
    - `POST /auth/logout`: Invalidate the current session.
    - `GET /auth/validate`: Verify the validity of a token.
    - `GET /auth/admin/check`: Verify if the current user has administrative privileges.

### 3.2 User Profile Management (User Service)
- **ID: FR-02**
- **Description:** Users can manage their personal information.
- **Requirements:**
    - Retrieve personal profile details.
    - Update name, email, and bio.
- **Endpoints:**
    - `GET /api/users/me`: Retrieve the current user's profile.
    - `PUT /api/users/me`: Update the current user's profile information.
    - `GET /api/teachers/my-courses`: List courses managed by the authenticated teacher.
    - `GET /api/students/my-courses`: List courses where the authenticated student is enrolled.

### 3.3 Course & Lesson Management (Course Service)
- **ID: FR-03**
- **Description:** The core engine for educational content.
- **Requirements:**
    - Teachers can create, update, and delete courses.
    - Courses consist of multiple lessons with specific ordering.
    - Public listing of available courses for all users.
    - Strategy-based authorization to ensure only owners/admins can modify content.
- **Endpoints:**
    - `POST /api/courses`: Create a new course (Teacher only).
    - `GET /api/courses`: Retrieve all available courses.
    - `GET /api/courses/my`: Retrieve courses related to the current user (filtered by role).
    - `GET /api/courses/{id}`: Retrieve full details of a specific course.
    - `PUT /api/courses/{id}`: Update course metadata (Owner only).
    - `DELETE /api/courses/{id}`: Permanently remove a course (Owner only).
    - `POST /api/courses/{courseId}/lessons`: Add a new lesson to a course (Owner only).
    - `GET /api/courses/{courseId}/lessons`: List all lessons associated with a course.
    - `PUT /api/courses/{courseId}/lessons/{lessonId}`: Update lesson content (Owner only).
    - `DELETE /api/courses/{courseId}/lessons/{lessonId}`: Remove a lesson (Owner only).

### 3.4 Enrollment Management (Course Service)
- **ID: FR-04**
- **Description:** Manages the relationship between students and courses.
- **Requirements:**
    - Students can request enrollment in a course.
    - Teachers can approve or reject enrollment requests.
    - Formal constraint: Students cannot enroll in courses they own (Teacher role).
- **Endpoints:**
    - `POST /api/enrollments`: Submit an enrollment request for a course.
    - `GET /api/enrollments/my-courses`: List all enrollments for the current student.
    - `GET /api/enrollments/pending`: List pending requests for a teacher's courses.
    - `POST /api/enrollments/{id}/approve`: Approve a student's enrollment (Teacher only).
    - `POST /api/enrollments/{id}/reject`: Reject a student's enrollment (Teacher only).

### 3.5 Media & Content Management (Upload Service)
- **ID: FR-05**
- **Description:** Handles file assets for lessons.
- **Requirements:**
    - Upload of PDFs and Video files.
    - Secure file retrieval via unique IDs.
    - Volume mapping for persistent storage in Docker environments.
- **Endpoints:**
    - `POST /api/uploads`: Upload a new media file (Teacher only).
    - `GET /api/uploads/{id}/download`: Stream/Download a specific media file.
    - `DELETE /api/uploads/{id}`: Delete a media asset from storage.

---

## 4. External Interface Requirements

### 4.1 User Interfaces
- **Web Application:** Responsive dashboard built with Next.js and Tailwind CSS.
- **Design System:** Consistent UI components using Shadcn UI and Radix UI.
- **Dynamic Views:** Interface must adapt based on the logged-in user's role.

### 4.2 Software Interfaces
- **PostgreSQL:** Primary relational database for all services.
- **Redis:** Used by Auth Service for token management.
- **Eureka:** Used for service discovery and load balancing.

### 4.3 Communication Interfaces
- **RESTful APIs:** JSON over HTTP for all service communication.
- **JWT:** Bearer tokens for secure authentication.
- **Gateway Headers:** X-Username and X-Role headers forwarded by the gateway to downstream services.

---

## 5. Other Non-functional Requirements

### 5.1 Performance Requirements
- **Latency:** API Gateway should route requests with sub-100ms overhead.
- **Scalability:** Services must be stateless to support horizontal scaling.

### 5.2 Safety & Security Requirements
- **Input Validation:** All API inputs must be validated using JSR-303/JSR-380 annotations.
- **Data Integrity:** Cascading deletes and foreign key constraints in PostgreSQL.
- **Role Enforcement:** Strict AOP-based checks for every sensitive endpoint.

### 5.3 Software Quality Attributes
- **Maintainability:** Adherence to SOLID principles and Clean Code practices.
- **Availability:** Microservices must be registered with Eureka for high availability.
- **Testability:** Decoupled architecture allowing for unit testing of individual services.
