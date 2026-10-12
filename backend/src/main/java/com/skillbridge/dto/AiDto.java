package com.skillbridge.dto;

import java.util.List;

public class AiDto {

    public static class ChatRequest {
        private String message;
        private Long roleId;
        private String context;

        public ChatRequest() {}
        public ChatRequest(String message, Long roleId, String context) {
            this.message = message;
            this.roleId = roleId;
            this.context = context;
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Long getRoleId() { return roleId; }
        public void setRoleId(Long roleId) { this.roleId = roleId; }
        public String getContext() { return context; }
        public void setContext(String context) { this.context = context; }
    }

    public static class RagSource {
        private String title;
        private String topic;
        private String snippet;
        private String sourceName;
        private String sourceUrl;
        private double relevanceScore;

        public RagSource() {}
        public RagSource(String title, String topic, String snippet, String sourceName, String sourceUrl, double relevanceScore) {
            this.title = title;
            this.topic = topic;
            this.snippet = snippet;
            this.sourceName = sourceName;
            this.sourceUrl = sourceUrl;
            this.relevanceScore = relevanceScore;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public String getSnippet() { return snippet; }
        public void setSnippet(String snippet) { this.snippet = snippet; }
        public String getSourceName() { return sourceName; }
        public void setSourceName(String sourceName) { this.sourceName = sourceName; }
        public String getSourceUrl() { return sourceUrl; }
        public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
        public double getRelevanceScore() { return relevanceScore; }
        public void setRelevanceScore(double relevanceScore) { this.relevanceScore = relevanceScore; }
    }

    public static class ChatResponse {
        private String answer;
        private String recommendationReason; // "Each recommendation comes with the reason for it"
        private List<String> actionableNextSteps;
        private List<RagSource> sources; // Curated, approved resources retrieved via RAG
        private String disclaimer = "AI output is treated as guidance, not a guaranteed career outcome.";

        public ChatResponse() {}

        public ChatResponse(String answer, String recommendationReason, List<String> actionableNextSteps, List<RagSource> sources) {
            this.answer = answer;
            this.recommendationReason = recommendationReason;
            this.actionableNextSteps = actionableNextSteps;
            this.sources = sources;
            this.disclaimer = "AI output is treated as guidance, not a guaranteed career outcome.";
        }

        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
        public String getRecommendationReason() { return recommendationReason; }
        public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }
        public List<String> getActionableNextSteps() { return actionableNextSteps; }
        public void setActionableNextSteps(List<String> actionableNextSteps) { this.actionableNextSteps = actionableNextSteps; }
        public List<RagSource> getSources() { return sources; }
        public void setSources(List<RagSource> sources) { this.sources = sources; }
        public String getDisclaimer() { return disclaimer; }
        public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }
    }
}
