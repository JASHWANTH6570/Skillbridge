package com.skillbridge.controller;

import com.skillbridge.dto.GapAnalysisDto;
import com.skillbridge.dto.SkillAssessmentDto;
import com.skillbridge.model.Skill;
import com.skillbridge.model.StudentSkill;
import com.skillbridge.security.UserPrincipal;
import com.skillbridge.service.SkillGapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills")
public class SkillAssessmentController {

    @Autowired
    private SkillGapService skillGapService;

    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillGapService.getAllSkills());
    }

    @GetMapping("/my-skills")
    public ResponseEntity<List<StudentSkill>> getMySkills(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(skillGapService.getStudentSkills(principal.getId()));
    }

    @PostMapping("/assess")
    public ResponseEntity<?> saveAssessment(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody SkillAssessmentDto.SaveAssessmentRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        skillGapService.saveStudentAssessment(principal.getId(), request);
        return ResponseEntity.ok(Map.of("message", "Skill assessment recorded successfully"));
    }

    @GetMapping("/gap-analysis/{roleId}")
    public ResponseEntity<GapAnalysisDto.GapAnalysisResponse> getGapAnalysis(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long roleId) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        GapAnalysisDto.GapAnalysisResponse analysis = skillGapService.analyzeSkillGap(principal.getId(), roleId);
        return ResponseEntity.ok(analysis);
    }
}
