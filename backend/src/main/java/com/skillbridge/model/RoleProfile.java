package com.skillbridge.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role_profiles")
public class RoleProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String department; // Engineering, Data, DevOps, Cloud

    @Column(length = 2000)
    private String description;

    private String experienceLevel; // Entry-Level, Mid-Level, Senior

    @OneToMany(mappedBy = "roleProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleSkillRequirement> skillRequirements = new ArrayList<>();

    public RoleProfile() {}

    public RoleProfile(String title, String department, String description, String experienceLevel) {
        this.title = title;
        this.department = department;
        this.description = description;
        this.experienceLevel = experienceLevel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }

    public List<RoleSkillRequirement> getSkillRequirements() { return skillRequirements; }
    public void setSkillRequirements(List<RoleSkillRequirement> skillRequirements) { this.skillRequirements = skillRequirements; }
}
