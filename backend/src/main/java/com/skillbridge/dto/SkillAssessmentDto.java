package com.skillbridge.dto;

import java.util.List;

public class SkillAssessmentDto {

    public static class SkillRating {
        private Long skillId;
        private String skillName;
        private int proficiencyLevel; // 1 to 5

        public SkillRating() {}
        public SkillRating(Long skillId, String skillName, int proficiencyLevel) {
            this.skillId = skillId;
            this.skillName = skillName;
            this.proficiencyLevel = proficiencyLevel;
        }

        public Long getSkillId() { return skillId; }
        public void setSkillId(Long skillId) { this.skillId = skillId; }
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        public int getProficiencyLevel() { return proficiencyLevel; }
        public void setProficiencyLevel(int proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }
    }

    public static class SaveAssessmentRequest {
        private Long targetRoleId;
        private List<SkillRating> skills;

        public SaveAssessmentRequest() {}
        public SaveAssessmentRequest(Long targetRoleId, List<SkillRating> skills) {
            this.targetRoleId = targetRoleId;
            this.skills = skills;
        }

        public Long getTargetRoleId() { return targetRoleId; }
        public void setTargetRoleId(Long targetRoleId) { this.targetRoleId = targetRoleId; }
        public List<SkillRating> getSkills() { return skills; }
        public void setSkills(List<SkillRating> skills) { this.skills = skills; }
    }
}
