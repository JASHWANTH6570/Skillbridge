package com.skillbridge.service;

import com.skillbridge.dto.GapAnalysisDto;
import com.skillbridge.dto.RoadmapDto;
import com.skillbridge.model.*;
import com.skillbridge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoadmapService {

    @Autowired
    private RoadmapRepository roadmapRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private RoleProfileRepository roleProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private SkillGapService skillGapService;

    @Transactional
    public RoadmapDto.RoadmapResponse generateRoadmap(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        RoleProfile roleProfile = roleProfileRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role profile not found: " + roleId));

        // Archive previous active roadmaps
        List<Roadmap> existingRoadmaps = roadmapRepository.findByUserIdOrderByCreatedAtDesc(userId);
        for (Roadmap r : existingRoadmaps) {
            if ("ACTIVE".equals(r.getStatus())) {
                r.setStatus("ARCHIVED");
                roadmapRepository.save(r);
            }
        }

        GapAnalysisDto.GapAnalysisResponse gapAnalysis = skillGapService.analyzeSkillGap(userId, roleId);

        String title = "Personalized " + roleProfile.getTitle() + " Mastery Roadmap";
        String summary = String.format("A targeted learning journey addressing %d identified skill gaps. Designed to boost role readiness from %d%% towards full competency.",
                gapAnalysis.getSkillGaps().stream().filter(g -> g.getGap() > 0).count(),
                gapAnalysis.getReadinessScore());

        Roadmap roadmap = new Roadmap(user, roleProfile, title, summary);
        roadmap = roadmapRepository.save(roadmap);

        // Build milestones based on ordered skill gaps
        List<GapAnalysisDto.SkillGapItem> gaps = gapAnalysis.getSkillGaps().stream()
                .filter(g -> g.getGap() > 0)
                .sorted((a, b) -> Integer.compare(b.getGap(), a.getGap()))
                .collect(Collectors.toList());

        List<Milestone> createdMilestones = new ArrayList<>();
        int weekCounter = 1;

        if (gaps.isEmpty()) {
            // Already proficient, create advanced capstone milestones
            Milestone capstone = new Milestone(
                    roadmap,
                    weekCounter++,
                    1,
                    "Advanced System Architecture & Industry Portfolio Showcase",
                    "Synthesize your full expertise by architecting an end-to-end production-grade system with CI/CD and metrics.",
                    "Full Stack Architecture",
                    "Build a multi-tenant SaaS application with automated deployment pipelines and observability dashboards.",
                    "Demonstrate senior engineering capabilities for direct hiring evaluation.",
                    "https://github.com, https://martinfowler.com/architecture"
            );
            createdMilestones.add(milestoneRepository.save(capstone));
        } else {
            for (int i = 0; i < gaps.size(); i++) {
                GapAnalysisDto.SkillGapItem item = gaps.get(i);
                List<Resource> matchingResources = resourceRepository.findBySkillNameIgnoreCase(item.getSkillName());
                String resourceLinks = matchingResources.stream()
                        .map(r -> r.getTitle() + " (" + r.getProvider() + ") - " + r.getUrl())
                        .limit(3)
                        .collect(Collectors.joining("; "));

                if (resourceLinks.isEmpty()) {
                    resourceLinks = "Official Documentation: https://docs.google.com; Hands-on Tutorials: https://dev.to";
                }

                String portfolioProject = generatePortfolioProjectForSkill(item.getSkillName(), roleProfile.getTitle());
                String outcome = String.format("Bridge proficiency gap from %d/5 to %d/5 in %s, proving capability with practical project delivery.",
                        item.getCurrentProficiency(), item.getRequiredProficiency(), item.getSkillName());

                Milestone milestone = new Milestone(
                        roadmap,
                        weekCounter++,
                        i + 1,
                        "Milestone " + (i + 1) + ": Deep Dive into " + item.getSkillName(),
                        String.format("Focused study and practical drills on %s to satisfy %s requirements for the %s profile.",
                                item.getSkillName(), item.getImportance(), roleProfile.getTitle()),
                        item.getSkillName(),
                        portfolioProject,
                        outcome,
                        resourceLinks
                );
                createdMilestones.add(milestoneRepository.save(milestone));
            }

            // Capstone milestone at the end
            Milestone finalCapstone = new Milestone(
                    roadmap,
                    weekCounter,
                    createdMilestones.size() + 1,
                    "Milestone " + (createdMilestones.size() + 1) + ": Comprehensive Capstone Portfolio Project",
                    "Integrate all acquired skills into an enterprise-ready portfolio project with comprehensive test coverage and documentation.",
                    "Applied Integration & System Design",
                    "Production-ready " + roleProfile.getTitle() + " Showcase Application on GitHub with live cloud demo",
                    "Deliver an end-to-end portfolio artifact demonstrating total job readiness.",
                    "https://github.com, https://docker.com, https://roadmap.sh"
            );
            createdMilestones.add(milestoneRepository.save(finalCapstone));
        }

        roadmap.setMilestones(createdMilestones);
        roadmap.setTotalMilestones(createdMilestones.size());
        roadmap.setCompletedMilestones(0);
        roadmap = roadmapRepository.save(roadmap);

        return mapToRoadmapResponse(roadmap);
    }

    public RoadmapDto.RoadmapResponse getActiveRoadmap(Long userId) {
        Optional<Roadmap> roadmapOpt = roadmapRepository.findFirstByUserIdAndStatusOrderByCreatedAtDesc(userId, "ACTIVE")
                .or(() -> roadmapRepository.findFirstByUserIdOrderByCreatedAtDesc(userId));

        return roadmapOpt.map(this::mapToRoadmapResponse).orElse(null);
    }

    @Transactional
    public RoadmapDto.RoadmapResponse toggleMilestoneCompletion(Long userId, Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found: " + milestoneId));

        Roadmap roadmap = milestone.getRoadmap();
        if (!roadmap.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to milestone");
        }

        milestone.setCompleted(!milestone.isCompleted());
        milestoneRepository.save(milestone);

        // Recalculate completed count
        List<Milestone> allMilestones = milestoneRepository.findByRoadmapIdOrderByOrderIndexAsc(roadmap.getId());
        long completed = allMilestones.stream().filter(Milestone::isCompleted).count();
        roadmap.setCompletedMilestones((int) completed);
        if (completed == allMilestones.size()) {
            roadmap.setStatus("COMPLETED");
        } else {
            roadmap.setStatus("ACTIVE");
        }
        roadmap.setUpdatedAt(LocalDateTime.now());
        roadmapRepository.save(roadmap);

        return mapToRoadmapResponse(roadmap);
    }

    @Transactional
    public RoadmapDto.MilestoneDto updateMilestone(Long userId, Long milestoneId, RoadmapDto.MilestoneEditRequest req) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found: " + milestoneId));

        if (!milestone.getRoadmap().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to milestone");
        }

        if (req.getTitle() != null) milestone.setTitle(req.getTitle());
        if (req.getDescription() != null) milestone.setDescription(req.getDescription());
        if (req.getFocusSkill() != null) milestone.setFocusSkill(req.getFocusSkill());
        if (req.getPortfolioProject() != null) milestone.setPortfolioProject(req.getPortfolioProject());
        if (req.getLearningOutcome() != null) milestone.setLearningOutcome(req.getLearningOutcome());
        if (req.getResourceLinks() != null) milestone.setResourceLinks(req.getResourceLinks());

        Milestone saved = milestoneRepository.save(milestone);
        return mapToMilestoneDto(saved);
    }

    private String generatePortfolioProjectForSkill(String skillName, String roleTitle) {
        String s = skillName.toLowerCase();
        if (s.contains("spring") || s.contains("java")) {
            return "Build a secure RESTful API microservice with Spring Boot 3, Spring Data JPA, JWT authentication, and Swagger OpenAPI documentation.";
        } else if (s.contains("react") || s.contains("frontend")) {
            return "Design and implement a responsive dashboard SPA using React, Vite, Tailwind CSS, and custom hooks for real-time data visualizer.";
        } else if (s.contains("docker") || s.contains("kubernetes") || s.contains("devops")) {
            return "Containerize a multi-tier application with Docker Compose, including health checks, volume persistence, and automated GitHub Actions CI/CD.";
        } else if (s.contains("sql") || s.contains("database")) {
            return "Design an optimized normalized relational database schema with indexing, transactions, and migration scripts using Liquibase/Flyway.";
        } else if (s.contains("rag") || s.contains("ai") || s.contains("llm")) {
            return "Develop a contextual RAG QA bot using embeddings, vector similarity search, and an LLM orchestration layer with fallback caching.";
        } else {
            return "Construct a hands-on project demonstrating core " + skillName + " patterns, modular code structure, and automated unit tests.";
        }
    }

    private RoadmapDto.RoadmapResponse mapToRoadmapResponse(Roadmap roadmap) {
        RoadmapDto.RoadmapResponse resp = new RoadmapDto.RoadmapResponse();
        resp.setId(roadmap.getId());
        resp.setRoleId(roadmap.getTargetRole().getId());
        resp.setRoleTitle(roadmap.getTargetRole().getTitle());
        resp.setTitle(roadmap.getTitle());
        resp.setSummary(roadmap.getSummary());
        resp.setTotalMilestones(roadmap.getTotalMilestones());
        resp.setCompletedMilestones(roadmap.getCompletedMilestones());

        double progress = roadmap.getTotalMilestones() > 0
                ? ((double) roadmap.getCompletedMilestones() / roadmap.getTotalMilestones()) * 100.0
                : 0.0;
        resp.setProgressPercentage(Math.round(progress * 10.0) / 10.0);
        resp.setStatus(roadmap.getStatus());
        resp.setCreatedAt(roadmap.getCreatedAt());
        resp.setUpdatedAt(roadmap.getUpdatedAt());

        List<Milestone> milestones = milestoneRepository.findByRoadmapIdOrderByOrderIndexAsc(roadmap.getId());
        resp.setMilestones(milestones.stream().map(this::mapToMilestoneDto).collect(Collectors.toList()));

        return resp;
    }

    private RoadmapDto.MilestoneDto mapToMilestoneDto(Milestone m) {
        RoadmapDto.MilestoneDto dto = new RoadmapDto.MilestoneDto();
        dto.setId(m.getId());
        dto.setWeekNumber(m.getWeekNumber());
        dto.setOrderIndex(m.getOrderIndex());
        dto.setTitle(m.getTitle());
        dto.setDescription(m.getDescription());
        dto.setFocusSkill(m.getFocusSkill());
        dto.setCompleted(m.isCompleted());
        dto.setPortfolioProject(m.getPortfolioProject());
        dto.setLearningOutcome(m.getLearningOutcome());
        dto.setResourceLinks(m.getResourceLinks());
        return dto;
    }
}
