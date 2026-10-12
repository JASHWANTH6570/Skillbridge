# SkillBridge 🚀
### AI-Powered Student Skill & Career Roadmap Platform

SkillBridge turns ambitious career goals into personalized, trackable, and milestone-driven learning journeys. Designed for students preparing for tech industry roles, it bridges the gap between fragmented course platforms, unclear skill requirements, and abandoned study plans.

---

## 🌟 Key Capabilities

1. **Target Role Selection**: Students set target roles (Full Stack Developer, AI/ML Engineer, Cloud & DevOps Specialist).
2. **Skill Assessment & Proficiency Benchmarking**: Students record their current proficiency across categorized competencies (Scale: 1 to 5).
3. **Automated Skill-Gap Analysis**: Real-time evaluation comparing current skills against defined role profiles, calculating **Career Readiness (%)**, identifying critical gaps, and formulating high-impact priorities.
4. **Dynamic Roadmap Generation**: Generates an ordered chronological weekly milestone plan complete with:
   - Specific competency focus
   - **Hands-on portfolio project deliverables**
   - **Staff-approved curated learning resources** (MDN, Spring.io, Docker Docs, etc.)
   - Dynamic progress tracking (checkboxes dynamically recompute completion %)
   - Milestone editing capability (students can customize their learning path)
5. **AI Career Assistant (RAG Grounded)**:
   - Uses **Retrieval-Augmented Generation (RAG)** over curated, admin-approved documentation
   - Explains the **recommendation reason / rationale** for every suggestion
   - Provides concrete actionable next steps
   - Includes transparent citations with relevance scores
   - Displays career disclaimer (*AI output is treated as guidance, not a guaranteed career outcome*)
6. **Staff Administrator Console**:
   - Program analytics: Cohort completion rate, total roadmaps generated, average readiness
   - Enrolled student oversight
   - Catalog management: add and remove approved curated learning materials

---

## 🏗 Layered Architecture

```
[ Frontend: React 18 + Vite + Tailwind CSS ]
                    |
              HTTP / REST (JWT Bearer Auth)
                    |
[ Backend: Spring Boot 3.3.5 REST API ]
  ├── Security Layer (Spring Security, JWT TokenProvider, BCrypt)
  ├── Controllers (Auth, Roles, Skills, Roadmaps, AI, Admin)
  ├── Services (SkillGapService, RoadmapService, RagService, AiAssistantService, AdminService)
  ├── Repositories (Spring Data JPA)
  └── Data Layer (H2 in-memory dev / MySQL 8.0 production)
```

---

## 🗓 6 Development Phases

- **Phase 1: Foundation**: Domain models (`User`, `RoleProfile`, `Skill`, `Roadmap`, `Milestone`, `Resource`, `KnowledgeChunk`), JPA repositories, and seed datasets.
- **Phase 2: Core APIs**: Skill gap analysis engine, readiness scoring algorithm, roadmap generation engine, and milestone lifecycle endpoints.
- **Phase 3: Frontend**: React + Vite SPA, responsive UI with Tailwind CSS, radar/bar comparison charts, weekly timeline, and editable milestones.
- **Phase 4: Security**: Stateless JWT authentication, BCrypt password hashing, role-based authorization (`ROLE_STUDENT`, `ROLE_ADMIN`), and CORS filters.
- **Phase 5: AI Integration & Testing**: RAG similarity search over curated knowledge chunks, grounded recommendation reason generator, JUnit 5 + Mockito test suites.
- **Phase 6: Release & Containerization**: Multi-stage Dockerfiles for frontend and backend, orchestrated with `docker-compose.yml` and MySQL 8.0.

---

## 🚀 Quick Start Guide

### Prerequisites
- **Node.js** (v18+) & **npm**
- **Java JDK** (21+)
- **Apache Maven** (3.8+)

### 1. Launch Spring Boot Backend
```powershell
cd backend
mvn spring-boot:run
```
*The backend starts at `http://localhost:8080`. H2 Database console is accessible at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:skillbridgedb`, user: `sa`).*

### 2. Launch React + Vite Frontend
```powershell
cd frontend
npm install
npm run dev
```
*The frontend starts at `http://localhost:5173` with automatic `/api` proxy to the Spring Boot backend.*

### 3. Run Automated Tests
```powershell
cd backend
mvn test
```
*Executes unit and integration tests for SkillGapService and RoadmapService using JUnit and Mockito.*

---

## 🔑 Pre-Seeded Demo Accounts

| Account Role | Username | Password | Notes |
|---|---|---|---|
| **Student** | `student` | `password123` | Pre-configured target role and baseline skills |
| **Administrator** | `admin` | `password123` | Full access to Admin Console, analytics & resources |

---

## 🐳 Docker Deployment
To launch the entire platform (MySQL 8.0, Spring Boot backend, and React frontend):
```bash
docker-compose up --build
```
- Frontend: `http://localhost:5173`
- Backend API: `http://localhost:8080`
- MySQL: `localhost:3306`
