package com.skillbridge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "role_skill_requirements")
public class RoleSkillRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_profile_id", nullable = false)
    @JsonIgnore
    private RoleProfile roleProfile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    // Expected proficiency from 1 (Novice) to 5 (Expert)
    private int requiredProficiency = 3;

    private String importance = "REQUIRED"; // REQUIRED, RECOMMENDED, NICE_TO_HAVE

    public RoleSkillRequirement() {}

    public RoleSkillRequirement(RoleProfile roleProfile, Skill skill, int requiredProficiency, String importance) {
        this.roleProfile = roleProfile;
        this.skill = skill;
        this.requiredProficiency = requiredProficiency;
        this.importance = importance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RoleProfile getRoleProfile() { return roleProfile; }
    public void setRoleProfile(RoleProfile roleProfile) { this.roleProfile = roleProfile; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public int getRequiredProficiency() { return requiredProficiency; }
    public void setRequiredProficiency(int requiredProficiency) { this.requiredProficiency = requiredProficiency; }

    public String getImportance() { return importance; }
    public void setImportance(String importance) { this.importance = importance; }
}
