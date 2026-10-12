package com.skillbridge.controller;

import com.skillbridge.model.RoleProfile;
import com.skillbridge.repository.RoleSkillRequirementRepository;
import com.skillbridge.service.SkillGapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleProfileController {

    @Autowired
    private SkillGapService skillGapService;

    @Autowired
    private RoleSkillRequirementRepository requirementRepository;

    @GetMapping
    public ResponseEntity<List<RoleProfile>> getAllRoles() {
        return ResponseEntity.ok(skillGapService.getAllRoleProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleProfile> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(skillGapService.getRoleProfileById(id));
    }

    @GetMapping("/{id}/requirements")
    public ResponseEntity<?> getRoleRequirements(@PathVariable Long id) {
        return ResponseEntity.ok(requirementRepository.findByRoleProfileId(id));
    }
}
