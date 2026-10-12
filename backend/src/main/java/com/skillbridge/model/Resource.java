package com.skillbridge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String type; // COURSE, DOCS, VIDEO, PROJECT, BOOK

    private String url;

    private String provider; // MDN, Spring.io, Coursera, FreeCodeCamp, etc.

    private int durationMinutes;

    private String difficulty; // BEGINNER, INTERMEDIATE, ADVANCED

    private String skillName;

    public Resource() {}

    public Resource(String title, String type, String url, String provider, int durationMinutes, String difficulty, String skillName) {
        this.title = title;
        this.type = type;
        this.url = url;
        this.provider = provider;
        this.durationMinutes = durationMinutes;
        this.difficulty = difficulty;
        this.skillName = skillName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
}
