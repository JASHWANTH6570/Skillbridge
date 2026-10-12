package com.skillbridge.dto;

import java.util.Map;

public class AdminDto {

    public static class SystemStatsResponse {
        private long totalUsers;
        private long totalStudents;
        private long totalRoles;
        private long totalRoadmaps;
        private long totalMilestones;
        private long completedMilestones;
        private double averageReadinessScore;
        private double overallCompletionRate;
        private Map<String, Long> studentsPerRole;

        public SystemStatsResponse() {}

        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public long getTotalStudents() { return totalStudents; }
        public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }
        public long getTotalRoles() { return totalRoles; }
        public void setTotalRoles(long totalRoles) { this.totalRoles = totalRoles; }
        public long getTotalRoadmaps() { return totalRoadmaps; }
        public void setTotalRoadmaps(long totalRoadmaps) { this.totalRoadmaps = totalRoadmaps; }
        public long getTotalMilestones() { return totalMilestones; }
        public void setTotalMilestones(long totalMilestones) { this.totalMilestones = totalMilestones; }
        public long getCompletedMilestones() { return completedMilestones; }
        public void setCompletedMilestones(long completedMilestones) { this.completedMilestones = completedMilestones; }
        public double getAverageReadinessScore() { return averageReadinessScore; }
        public void setAverageReadinessScore(double averageReadinessScore) { this.averageReadinessScore = averageReadinessScore; }
        public double getOverallCompletionRate() { return overallCompletionRate; }
        public void setOverallCompletionRate(double overallCompletionRate) { this.overallCompletionRate = overallCompletionRate; }
        public Map<String, Long> getStudentsPerRole() { return studentsPerRole; }
        public void setStudentsPerRole(Map<String, Long> studentsPerRole) { this.studentsPerRole = studentsPerRole; }
    }
}
