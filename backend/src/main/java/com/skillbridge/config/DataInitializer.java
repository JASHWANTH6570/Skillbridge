package com.skillbridge.config;

import com.skillbridge.model.*;
import com.skillbridge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleProfileRepository roleProfileRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private RoleSkillRequirementRepository requirementRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private KnowledgeChunkRepository knowledgeChunkRepository;

    @Autowired
    private StudentSkillRepository studentSkillRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleProfileRepository.count() > 0) {
            return; // Data already initialized
        }

        // 1. Create Skills
        Skill springBoot = skillRepository.save(new Skill("Spring Boot", "Backend", "Enterprise Java microservices, REST APIs, dependency injection, Spring Data JPA."));
        Skill react = skillRepository.save(new Skill("React & Vite", "Frontend", "Component lifecycle, state management, hooks, modern virtual DOM UI frameworks."));
        Skill docker = skillRepository.save(new Skill("Docker & Containers", "DevOps", "Containerization, multi-stage builds, Docker Compose, isolation and runtime environments."));
        Skill sql = skillRepository.save(new Skill("SQL & Database Design", "Database", "Relational modeling, indexing, ACID transactions, queries and normalization in MySQL/PostgreSQL."));
        Skill restApi = skillRepository.save(new Skill("RESTful API & Security", "Backend", "HTTP semantics, JWT tokens, OAuth2, BCrypt, rate limiting, and OpenAPI Swagger documentation."));
        Skill git = skillRepository.save(new Skill("Git & CI/CD", "DevOps", "Version control, branching strategies, GitHub Actions, automated test and release pipelines."));
        Skill python = skillRepository.save(new Skill("Python Programming", "Core", "Data structures, algorithms, asynchronous programming, scripting and package ecosystems."));
        Skill ragAi = skillRepository.save(new Skill("LLMs & RAG Architectures", "AI/ML", "Prompt engineering, semantic embeddings, vector retrieval, contextual grounding, LLM pipelines."));
        Skill k8s = skillRepository.save(new Skill("Kubernetes & Cloud Deployments", "DevOps", "Cluster orchestration, deployments, services, ingress controllers, Helm charts."));
        Skill systemDesign = skillRepository.save(new Skill("System Design & Scalability", "Architecture", "Distributed systems, caching, message queues, load balancers, microservice resilience."));

        // 2. Create Target Role Profiles
        RoleProfile fullstack = roleProfileRepository.save(new RoleProfile(
                "Full Stack Developer (Java & React)",
                "Software Engineering",
                "Builds modern enterprise web applications spanning robust Spring Boot backends and interactive React frontends with full security and containerization.",
                "Entry-to-Mid Level"
        ));

        RoleProfile aiEngineer = roleProfileRepository.save(new RoleProfile(
                "AI & Machine Learning Engineer",
                "Applied Intelligence",
                "Designs and integrates LLM-driven applications, semantic retrieval systems (RAG), and data-driven intelligent pipelines.",
                "Entry-to-Mid Level"
        ));

        RoleProfile devops = roleProfileRepository.save(new RoleProfile(
                "Cloud & DevOps Engineer",
                "Infrastructure & Platform",
                "Automates cloud infrastructure, CI/CD pipelines, container orchestration, and system observability.",
                "Entry-to-Mid Level"
        ));

        // 3. Map Requirements to Full Stack Role
        requirementRepository.save(new RoleSkillRequirement(fullstack, springBoot, 4, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(fullstack, react, 4, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(fullstack, sql, 3, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(fullstack, restApi, 4, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(fullstack, git, 3, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(fullstack, docker, 3, "RECOMMENDED"));
        requirementRepository.save(new RoleSkillRequirement(fullstack, systemDesign, 3, "RECOMMENDED"));

        // Map Requirements to AI Engineer Role
        requirementRepository.save(new RoleSkillRequirement(aiEngineer, python, 4, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(aiEngineer, ragAi, 4, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(aiEngineer, restApi, 3, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(aiEngineer, sql, 3, "RECOMMENDED"));
        requirementRepository.save(new RoleSkillRequirement(aiEngineer, docker, 3, "RECOMMENDED"));

        // Map Requirements to DevOps Role
        requirementRepository.save(new RoleSkillRequirement(devops, docker, 4, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(devops, k8s, 4, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(devops, git, 4, "REQUIRED"));
        requirementRepository.save(new RoleSkillRequirement(devops, systemDesign, 3, "RECOMMENDED"));

        // 4. Create Curated Resources
        resourceRepository.save(new Resource(
                "Building RESTful Services with Spring Boot 3",
                "COURSE",
                "https://spring.io/guides/tutorials/rest/",
                "Spring.io Official",
                240,
                "INTERMEDIATE",
                "Spring Boot"
        ));
        resourceRepository.save(new Resource(
                "React Complete Guide: Modern Hooks & State",
                "COURSE",
                "https://react.dev/learn",
                "React Official Docs",
                300,
                "BEGINNER",
                "React & Vite"
        ));
        resourceRepository.save(new Resource(
                "Docker for Java & Full Stack Developers",
                "TUTORIAL",
                "https://docs.docker.com/get-started/",
                "Docker Docs",
                180,
                "BEGINNER",
                "Docker & Containers"
        ));
        resourceRepository.save(new Resource(
                "Database Normalization and Indexing Deep Dive",
                "DOCS",
                "https://dev.mysql.com/doc/refman/8.0/en/optimization-indexes.html",
                "MySQL Official",
                150,
                "INTERMEDIATE",
                "SQL & Database Design"
        ));
        resourceRepository.save(new Resource(
                "Retrieval-Augmented Generation (RAG) Architecture Handbook",
                "DOCS",
                "https://cloud.google.com/use-cases/retrieval-augmented-generation",
                "Google Cloud Architecture",
                210,
                "INTERMEDIATE",
                "LLMs & RAG Architectures"
        ));

        // 5. Seed RAG Curated Knowledge Chunks
        knowledgeChunkRepository.save(new KnowledgeChunk(
                "Spring Boot Architecture",
                "Enterprise Layered Architecture with Spring Data JPA & Spring Security",
                "In enterprise Spring Boot applications, strict separation between Controller, Service, and Repository layers is crucial. Controllers handle HTTP contract validation and status codes. Services contain core business logic and transaction management (@Transactional). Repositories interface with MySQL via Spring Data JPA. Security must enforce stateless JWT Bearer token authentication with BCrypt hashing for credentials.",
                "SkillBridge Curated Curriculum",
                "https://docs.spring.io/spring-boot/docs/current/reference/html/",
                "spring, boot, java, rest, jpa, security, jwt, bcrypt"
        ));

        knowledgeChunkRepository.save(new KnowledgeChunk(
                "React Frontend Standards",
                "Modern Single Page Architecture with Vite and Component State",
                "React applications built with Vite offer instant Hot Module Replacement (HMR) and optimized rollup production bundles. State management should leverage React context for authentication and localized useState/useEffect for component data. Responsive design should use modern CSS frameworks like Tailwind CSS, ensuring clean accessibility and cross-device responsiveness.",
                "SkillBridge Frontend Guidelines",
                "https://react.dev/reference/react",
                "react, vite, tailwind, frontend, state, ui, components"
        ));

        knowledgeChunkRepository.save(new KnowledgeChunk(
                "RAG Systems & Embeddings",
                "Retrieval-Augmented Generation Implementation Principles",
                "RAG grounds Large Language Models on verified external domain knowledge rather than hallucinated facts. The process splits documents into semantic chunks, indexes them via vector embeddings and keyword indices, and retrieves top-k relevant snippets upon student query. The retrieved context is formatted into prompt templates with strict disclaimers that AI output is career guidance, not guaranteed hiring outcomes.",
                "Google DeepMind AI Engineering Guide",
                "https://cloud.google.com/vertex-ai/docs/generative-ai/grounding/overview",
                "rag, llm, embeddings, retrieval, guidance, ai assistant, semantic"
        ));

        knowledgeChunkRepository.save(new KnowledgeChunk(
                "Skill Gap & Career Readiness",
                "Methodology for Evaluating Skill Gaps and Milestone Planning",
                "SkillBridge computes career readiness by matching student-reported proficiencies against target role expectations. Gaps are categorized into Critical (gap >= 2), Moderate (gap = 1), and Met (gap = 0). The roadmap engine prioritizes highest-gap required skills first, generating weekly milestones complete with hands-on portfolio deliverables to verify competencies.",
                "SkillBridge Career Methodology",
                "https://skillbridge.edu/curriculum/roadmap-framework",
                "skill gap, roadmap, readiness score, milestone, portfolio, career"
        ));

        // 6. Create Users
        User adminUser = new User(
                "admin",
                "admin@skillbridge.edu",
                passwordEncoder.encode("password123"),
                "SkillBridge Administrator",
                RoleType.ROLE_ADMIN
        );
        userRepository.save(adminUser);

        User studentUser = new User(
                "student",
                "student@skillbridge.edu",
                passwordEncoder.encode("password123"),
                "Alex Chen (Student)",
                RoleType.ROLE_STUDENT
        );
        studentUser.setTargetRoleId(fullstack.getId());
        User savedStudent = userRepository.save(studentUser);

        // Pre-seed some baseline student skills for demonstration
        studentSkillRepository.save(new StudentSkill(savedStudent, springBoot, 2));
        studentSkillRepository.save(new StudentSkill(savedStudent, react, 1));
        studentSkillRepository.save(new StudentSkill(savedStudent, sql, 2));
        studentSkillRepository.save(new StudentSkill(savedStudent, restApi, 2));
        studentSkillRepository.save(new StudentSkill(savedStudent, git, 3));
    }
}
