package com.skillbridge.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RoadmapDto {

    public static class MilestoneDto {
        private Long id;
        private int weekNumber;
        private int orderIndex;
        private String title;
        private String description;
        private String focusSkill;
        private boolean completed;
        private String portfolioProject;
        private String learningOutcome;
        private String resourceLinks;

        public MilestoneDto() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
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

    public static class RoadmapResponse {
        private Long id;
        private Long roleId;
        private String roleTitle;
        private String title;
        private String summary;
        private int totalMilestones;
        private int completedMilestones;
        private double progressPercentage;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<MilestoneDto> milestones;

        public RoadmapResponse() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getRoleId() { return roleId; }
        public void setRoleId(Long roleId) { this.roleId = roleId; }
        public String getRoleTitle() { return roleTitle; }
        public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public int getTotalMilestones() { return totalMilestones; }
        public void setTotalMilestones(int totalMilestones) { this.totalMilestones = totalMilestones; }
        public int getCompletedMilestones() { return completedMilestones; }
        public void setCompletedMilestones(int completedMilestones) { this.completedMilestones = completedMilestones; }
        public double getProgressPercentage() { return progressPercentage; }
        public void setProgressPercentage(double progressPercentage) { this.progressPercentage = progressPercentage; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public List<MilestoneDto> getMilestones() { return milestones; }
        public void setMilestones(List<MilestoneDto> milestones) { this.milestones = milestones; }
    }

    public static class MilestoneEditRequest {
        private String title;
        private String description;
        private String focusSkill;
        private String portfolioProject;
        private String learningOutcome;
        private String resourceLinks;

        public MilestoneEditRequest() {}

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getFocusSkill() { return focusSkill; }
        public void setFocusSkill(String focusSkill) { this.focusSkill = focusSkill; }
        public String getPortfolioProject() { return portfolioProject; }
        public void setPortfolioProject(String portfolioProject) { this.portfolioProject = portfolioProject; }
        public String getLearningOutcome() { return learningOutcome; }
        public void setLearningOutcome(String learningOutcome) { this.learningOutcome = learningOutcome; }
        public String getResourceLinks() { return resourceLinks; }
        public void setResourceLinks(String resourceLinks) { this.resourceLinks = resourceLinks; }
    }
}
