package com.skillbridge.repository;

import com.skillbridge.model.RoleSkillRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleSkillRequirementRepository extends JpaRepository<RoleSkillRequirement, Long> {
    List<RoleSkillRequirement> findByRoleProfileId(Long roleProfileId);
}
