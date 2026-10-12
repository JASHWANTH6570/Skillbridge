package com.skillbridge.controller;

import com.skillbridge.dto.AdminDto;
import com.skillbridge.model.KnowledgeChunk;
import com.skillbridge.model.Resource;
import com.skillbridge.model.User;
import com.skillbridge.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<AdminDto.SystemStatsResponse> getSystemStats() {
        return ResponseEntity.ok(adminService.getSystemStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/resources")
    public ResponseEntity<List<Resource>> getAllResources() {
        return ResponseEntity.ok(adminService.getAllResources());
    }

    @PostMapping("/resources")
    public ResponseEntity<Resource> createResource(@RequestBody Resource resource) {
        return ResponseEntity.ok(adminService.createResource(resource));
    }

    @DeleteMapping("/resources/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable Long id) {
        adminService.deleteResource(id);
        return ResponseEntity.ok(Map.of("message", "Resource deleted successfully"));
    }

    @GetMapping("/knowledge")
    public ResponseEntity<List<KnowledgeChunk>> getAllKnowledgeChunks() {
        return ResponseEntity.ok(adminService.getAllKnowledgeChunks());
    }

    @PostMapping("/knowledge")
    public ResponseEntity<KnowledgeChunk> createKnowledgeChunk(@RequestBody KnowledgeChunk chunk) {
        return ResponseEntity.ok(adminService.createKnowledgeChunk(chunk));
    }
}
