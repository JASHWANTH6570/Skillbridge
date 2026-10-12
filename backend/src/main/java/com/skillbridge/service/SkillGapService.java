package com.skillbridge.service;

import com.skillbridge.dto.GapAnalysisDto;
import com.skillbridge.dto.SkillAssessmentDto;
import com.skillbridge.model.*;
import com.skillbridge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillGapService {

    @Autowired
    private RoleProfileRepository roleProfileRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private RoleSkillRequirementRepository requirementRepository;

    @Autowired
    private StudentSkillRepository studentSkillRepository;

    @Autowired
    private UserRepository userRepository;

    public List<RoleProfile> getAllRoleProfiles() {
        return roleProfileRepository.findAll();
    }

    public RoleProfile getRoleProfileById(Long id) {
        return roleProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role profile not found with id: " + id));
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Transactional
    public void saveStudentAssessment(Long userId, SkillAssessmentDto.SaveAssessmentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (request.getTargetRoleId() != null) {
            user.setTargetRoleId(request.getTargetRoleId());
            userRepository.save(user);
        }

        for (SkillAssessmentDto.SkillRating rating : request.getSkills()) {
            Skill skill = skillRepository.findById(rating.getSkillId())
                    .orElseThrow(() -> new RuntimeException("Skill not found: " + rating.getSkillId()));

            Optional<StudentSkill> existing = studentSkillRepository.findByUserIdAndSkillId(userId, skill.getId());
            if (existing.isPresent()) {
                StudentSkill studentSkill = existing.get();
                studentSkill.setProficiencyLevel(Math.max(1, Math.min(5, rating.getProficiencyLevel())));
                studentSkill.setUpdatedAt(LocalDateTime.now());
                studentSkillRepository.save(studentSkill);
            } else {
                StudentSkill newSkill = new StudentSkill(user, skill, Math.max(1, Math.min(5, rating.getProficiencyLevel())));
                studentSkillRepository.save(newSkill);
            }
        }
    }

    public List<StudentSkill> getStudentSkills(Long userId) {
        return studentSkillRepository.findByUserId(userId);
    }

    public GapAnalysisDto.GapAnalysisResponse analyzeSkillGap(Long userId, Long roleProfileId) {
        RoleProfile roleProfile = roleProfileRepository.findById(roleProfileId)
                .orElseThrow(() -> new RuntimeException("Role profile not found: " + roleProfileId));

        List<RoleSkillRequirement> requirements = requirementRepository.findByRoleProfileId(roleProfileId);
        List<StudentSkill> studentSkills = studentSkillRepository.findByUserId(userId);

        Map<Long, Integer> studentSkillLevels = studentSkills.stream()
                .collect(Collectors.toMap(ss -> ss.getSkill().getId(), StudentSkill::getProficiencyLevel, (v1, v2) -> v2));

        List<GapAnalysisDto.SkillGapItem> gapItems = new ArrayList<>();
        int totalRequiredPoints = 0;
        int currentAchievedPoints = 0;
        int skillsMet = 0;
        int criticalGaps = 0;

        for (RoleSkillRequirement req : requirements) {
            Skill skill = req.getSkill();
            int currentLevel = studentSkillLevels.getOrDefault(skill.getId(), 0);
            int requiredLevel = req.getRequiredProficiency();

            totalRequiredPoints += requiredLevel;
            currentAchievedPoints += Math.min(currentLevel, requiredLevel);

            GapAnalysisDto.SkillGapItem item = new GapAnalysisDto.SkillGapItem(
                    skill.getId(),
                    skill.getName(),
                    skill.getCategory(),
                    currentLevel,
                    requiredLevel,
                    req.getImportance()
            );

            if (item.getGap() == 0) {
                skillsMet++;
            } else if (item.getGap() >= 2) {
                criticalGaps++;
            }

            gapItems.add(item);
        }

        // Sort items by gap descending, then required importance
        gapItems.sort((a, b) -> Integer.compare(b.getGap(), a.getGap()));

        int readinessScore = totalRequiredPoints > 0 ? (int) Math.round(((double) currentAchievedPoints / totalRequiredPoints) * 100) : 100;

        List<String> recommendations = new ArrayList<>();
        List<GapAnalysisDto.SkillGapItem> largestGaps = gapItems.stream().filter(g -> g.getGap() > 0).limit(4).toList();
        for (GapAnalysisDto.SkillGapItem gap : largestGaps) {
            recommendations.add(String.format("Prioritize '%s' (%s): Current level %d/5, Target role requires %d/5.",
                    gap.getSkillName(), gap.getImportance(), gap.getCurrentProficiency(), gap.getRequiredProficiency()));
        }

        GapAnalysisDto.GapAnalysisResponse response = new GapAnalysisDto.GapAnalysisResponse();
        response.setRoleId(roleProfile.getId());
        response.setRoleTitle(roleProfile.getTitle());
        response.setRoleDepartment(roleProfile.getDepartment());
        response.setExperienceLevel(roleProfile.getExperienceLevel());
        response.setReadinessScore(readinessScore);
        response.setTotalRequiredSkills(requirements.size());
        response.setSkillsMetCount(skillsMet);
        response.setCriticalGapsCount(criticalGaps);
        response.setSkillGaps(gapItems);
        response.setTopPriorityRecommendations(recommendations);

        return response;
    }
}
