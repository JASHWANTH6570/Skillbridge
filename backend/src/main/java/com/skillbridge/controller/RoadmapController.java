package com.skillbridge.controller;

import com.skillbridge.dto.RoadmapDto;
import com.skillbridge.security.UserPrincipal;
import com.skillbridge.service.RoadmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/roadmaps")
public class RoadmapController {

    @Autowired
    private RoadmapService roadmapService;

    @PostMapping("/generate/{roleId}")
    public ResponseEntity<RoadmapDto.RoadmapResponse> generateRoadmap(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long roleId) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        RoadmapDto.RoadmapResponse response = roadmapService.generateRoadmap(principal.getId(), roleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveRoadmap(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        RoadmapDto.RoadmapResponse response = roadmapService.getActiveRoadmap(principal.getId());
        if (response == null) {
            return ResponseEntity.ok(Map.of("message", "No active roadmap found. Please generate one."));
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/milestones/{milestoneId}/toggle")
    public ResponseEntity<RoadmapDto.RoadmapResponse> toggleMilestone(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long milestoneId) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        RoadmapDto.RoadmapResponse response = roadmapService.toggleMilestoneCompletion(principal.getId(), milestoneId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/milestones/{milestoneId}")
    public ResponseEntity<RoadmapDto.MilestoneDto> updateMilestone(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long milestoneId,
            @RequestBody RoadmapDto.MilestoneEditRequest req) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        RoadmapDto.MilestoneDto updated = roadmapService.updateMilestone(principal.getId(), milestoneId, req);
        return ResponseEntity.ok(updated);
    }
}
