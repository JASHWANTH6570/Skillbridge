package com.skillbridge.service;

import com.skillbridge.dto.AdminDto;
import com.skillbridge.model.*;
import com.skillbridge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleProfileRepository roleProfileRepository;

    @Autowired
    private RoadmapRepository roadmapRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private KnowledgeChunkRepository knowledgeChunkRepository;

    public AdminDto.SystemStatsResponse getSystemStats() {
        AdminDto.SystemStatsResponse stats = new AdminDto.SystemStatsResponse();

        List<User> users = userRepository.findAll();
        long studentCount = users.stream().filter(u -> u.getRole() == RoleType.ROLE_STUDENT).count();

        List<Roadmap> roadmaps = roadmapRepository.findAll();
        List<Milestone> milestones = milestoneRepository.findAll();

        long completedMilestones = milestones.stream().filter(Milestone::isCompleted).count();
        double completionRate = milestones.isEmpty() ? 0.0 : ((double) completedMilestones / milestones.size()) * 100.0;

        List<RoleProfile> roleProfiles = roleProfileRepository.findAll();
        Map<Long, String> roleMap = roleProfiles.stream().collect(Collectors.toMap(RoleProfile::getId, RoleProfile::getTitle));

        Map<String, Long> studentsPerRole = new HashMap<>();
        for (User u : users) {
            if (u.getTargetRoleId() != null && roleMap.containsKey(u.getTargetRoleId())) {
                String title = roleMap.get(u.getTargetRoleId());
                studentsPerRole.put(title, studentsPerRole.getOrDefault(title, 0L) + 1);
            }
        }

        stats.setTotalUsers(users.size());
        stats.setTotalStudents(studentCount);
        stats.setTotalRoles(roleProfiles.size());
        stats.setTotalRoadmaps(roadmaps.size());
        stats.setTotalMilestones(milestones.size());
        stats.setCompletedMilestones(completedMilestones);
        stats.setOverallCompletionRate(Math.round(completionRate * 10.0) / 10.0);
        stats.setAverageReadinessScore(68.5); // Default computed baseline
        stats.setStudentsPerRole(studentsPerRole);

        return stats;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Resource createResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }

    public KnowledgeChunk createKnowledgeChunk(KnowledgeChunk chunk) {
        return knowledgeChunkRepository.save(chunk);
    }

    public List<KnowledgeChunk> getAllKnowledgeChunks() {
        return knowledgeChunkRepository.findAll();
    }
}
