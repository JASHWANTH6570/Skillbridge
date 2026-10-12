package com.skillbridge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "knowledge_chunks")
public class KnowledgeChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String title;

    @Column(length = 4000, nullable = false)
    private String content;

    private String sourceName;

    private String sourceUrl;

    @Column(length = 1000)
    private String keywords; // Comma separated for lexical & semantic vector indexing

    private boolean approvedByAdmin = true;

    public KnowledgeChunk() {}

    public KnowledgeChunk(String topic, String title, String content, String sourceName, String sourceUrl, String keywords) {
        this.topic = topic;
        this.title = title;
        this.content = content;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
        this.keywords = keywords;
        this.approvedByAdmin = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }

    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public boolean isApprovedByAdmin() { return approvedByAdmin; }
    public void setApprovedByAdmin(boolean approvedByAdmin) { this.approvedByAdmin = approvedByAdmin; }
}
