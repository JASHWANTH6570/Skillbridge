package com.skillbridge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "milestones")
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    @JsonIgnore
    private Roadmap roadmap;

    private int weekNumber; // e.g. Week 1, Week 2...
    private int orderIndex; // 1, 2, 3...

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String focusSkill;

    private boolean completed = false;

    @Column(length = 1000)
    private String portfolioProject; // Suggested hands-on practical project

    @Column(length = 1000)
    private String learningOutcome;

    // Direct comma-separated or JSON list of curated resource links/titles
    @Column(length = 2000)
    private String resourceLinks;

    public Milestone() {}

    public Milestone(Roadmap roadmap, int weekNumber, int orderIndex, String title, String description, String focusSkill, String portfolioProject, String learningOutcome, String resourceLinks) {
        this.roadmap = roadmap;
        this.weekNumber = weekNumber;
        this.orderIndex = orderIndex;
        this.title = title;
        this.description = description;
        this.focusSkill = focusSkill;
        this.portfolioProject = portfolioProject;
        this.learningOutcome = learningOutcome;
        this.resourceLinks = resourceLinks;
        this.completed = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Roadmap getRoadmap() { return roadmap; }
    public void setRoadmap(Roadmap roadmap) { this.roadmap = roadmap; }

    public int getWeekNumber() { return weekNumber; }
    public void setWeekNumber(int weekNumber) { this.weekNumber = weekNumber; }

    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFocusSkill() { return focusSkill; }
    public void setFocusSkill(String focusSkill) { this.focusSkill = focusSkill; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getPortfolioProject() { return portfolioProject; }
    public void setPortfolioProject(String portfolioProject) { this.portfolioProject = portfolioProject; }

    public String getLearningOutcome() { return learningOutcome; }
    public void setLearningOutcome(String learningOutcome) { this.learningOutcome = learningOutcome; }

    public String getResourceLinks() { return resourceLinks; }
    public void setResourceLinks(String resourceLinks) { this.resourceLinks = resourceLinks; }
}
