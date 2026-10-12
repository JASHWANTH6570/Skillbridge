package com.skillbridge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roadmaps")
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_profile_id", nullable = false)
    private RoleProfile targetRole;

    private String title;

    @Column(length = 2000)
    private String summary;

    private int totalMilestones;
    private int completedMilestones;

    private String status = "ACTIVE"; // ACTIVE, COMPLETED, ARCHIVED

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<Milestone> milestones = new ArrayList<>();

    public Roadmap() {}

    public Roadmap(User user, RoleProfile targetRole, String title, String summary) {
        this.user = user;
        this.targetRole = targetRole;
        this.title = title;
        this.summary = summary;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public RoleProfile getTargetRole() { return targetRole; }
    public void setTargetRole(RoleProfile targetRole) { this.targetRole = targetRole; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public int getTotalMilestones() { return totalMilestones; }
    public void setTotalMilestones(int totalMilestones) { this.totalMilestones = totalMilestones; }

    public int getCompletedMilestones() { return completedMilestones; }
    public void setCompletedMilestones(int completedMilestones) { this.completedMilestones = completedMilestones; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Milestone> getMilestones() { return milestones; }
    public void setMilestones(List<Milestone> milestones) { this.milestones = milestones; }
}
