package com.skillbridge.dto;

import java.util.List;

public class GapAnalysisDto {

    public static class SkillGapItem {
        private Long skillId;
        private String skillName;
        private String category;
        private int currentProficiency;
        private int requiredProficiency;
        private int gap; // required - current (if > 0)
        private String importance; // REQUIRED, RECOMMENDED, NICE_TO_HAVE
        private String status; // MET, MODERATE_GAP, CRITICAL_GAP

        public SkillGapItem() {}

        public SkillGapItem(Long skillId, String skillName, String category, int currentProficiency, int requiredProficiency, String importance) {
            this.skillId = skillId;
            this.skillName = skillName;
            this.category = category;
            this.currentProficiency = currentProficiency;
            this.requiredProficiency = requiredProficiency;
            this.gap = Math.max(0, requiredProficiency - currentProficiency);
            this.importance = importance;
            if (gap == 0) {
                this.status = "MET";
            } else if (gap == 1) {
                this.status = "MODERATE_GAP";
            } else {
                this.status = "CRITICAL_GAP";
            }
        }

        public Long getSkillId() { return skillId; }
        public void setSkillId(Long skillId) { this.skillId = skillId; }
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public int getCurrentProficiency() { return currentProficiency; }
        public void setCurrentProficiency(int currentProficiency) { this.currentProficiency = currentProficiency; }
        public int getRequiredProficiency() { return requiredProficiency; }
        public void setRequiredProficiency(int requiredProficiency) { this.requiredProficiency = requiredProficiency; }
        public int getGap() { return gap; }
        public void setGap(int gap) { this.gap = gap; }
        public String getImportance() { return importance; }
        public void setImportance(String importance) { this.importance = importance; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class GapAnalysisResponse {
        private Long roleId;
        private String roleTitle;
        private String roleDepartment;
        private String experienceLevel;
        private int readinessScore; // 0 to 100%
        private int totalRequiredSkills;
        private int skillsMetCount;
        private int criticalGapsCount;
        private List<SkillGapItem> skillGaps;
        private List<String> topPriorityRecommendations;

        public GapAnalysisResponse() {}

        public Long getRoleId() { return roleId; }
        public void setRoleId(Long roleId) { this.roleId = roleId; }
        public String getRoleTitle() { return roleTitle; }
        public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }
        public String getRoleDepartment() { return roleDepartment; }
        public void setRoleDepartment(String roleDepartment) { this.roleDepartment = roleDepartment; }
        public String getExperienceLevel() { return experienceLevel; }
        public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }
        public int getReadinessScore() { return readinessScore; }
        public void setReadinessScore(int readinessScore) { this.readinessScore = readinessScore; }
        public int getTotalRequiredSkills() { return totalRequiredSkills; }
        public void setTotalRequiredSkills(int totalRequiredSkills) { this.totalRequiredSkills = totalRequiredSkills; }
        public int getSkillsMetCount() { return skillsMetCount; }
        public void setSkillsMetCount(int skillsMetCount) { this.skillsMetCount = skillsMetCount; }
        public int getCriticalGapsCount() { return criticalGapsCount; }
        public void setCriticalGapsCount(int criticalGapsCount) { this.criticalGapsCount = criticalGapsCount; }
        public List<SkillGapItem> getSkillGaps() { return skillGaps; }
        public void setSkillGaps(List<SkillGapItem> skillGaps) { this.skillGaps = skillGaps; }
        public List<String> getTopPriorityRecommendations() { return topPriorityRecommendations; }
        public void setTopPriorityRecommendations(List<String> topPriorityRecommendations) { this.topPriorityRecommendations = topPriorityRecommendations; }
    }
}
