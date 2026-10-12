package com.skillbridge.service;

import com.skillbridge.dto.GapAnalysisDto;
import com.skillbridge.model.*;
import com.skillbridge.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkillGapServiceTest {

    @Mock
    private RoleProfileRepository roleProfileRepository;

    @Mock
    private RoleSkillRequirementRepository requirementRepository;

    @Mock
    private StudentSkillRepository studentSkillRepository;

    @InjectMocks
    private SkillGapService skillGapService;

    private RoleProfile roleProfile;
    private Skill javaSkill;
    private Skill reactSkill;

    @BeforeEach
    void setUp() {
        roleProfile = new RoleProfile("Full Stack Developer", "Engineering", "Desc", "Entry");
        roleProfile.setId(1L);

        javaSkill = new Skill("Java", "Backend", "Core Java");
        javaSkill.setId(10L);

        reactSkill = new Skill("React", "Frontend", "React framework");
        reactSkill.setId(20L);
    }

    @Test
    void testAnalyzeSkillGap_CalculatesCorrectReadiness() {
        RoleSkillRequirement req1 = new RoleSkillRequirement(roleProfile, javaSkill, 4, "REQUIRED");
        RoleSkillRequirement req2 = new RoleSkillRequirement(roleProfile, reactSkill, 4, "REQUIRED");

        User user = new User("testuser", "test@test.com", "pass", "Test", RoleType.ROLE_STUDENT);
        user.setId(100L);

        StudentSkill studentSkill1 = new StudentSkill(user, javaSkill, 2); // gap 2
        StudentSkill studentSkill2 = new StudentSkill(user, reactSkill, 4); // gap 0

        when(roleProfileRepository.findById(1L)).thenReturn(Optional.of(roleProfile));
        when(requirementRepository.findByRoleProfileId(1L)).thenReturn(List.of(req1, req2));
        when(studentSkillRepository.findByUserId(100L)).thenReturn(List.of(studentSkill1, studentSkill2));

        GapAnalysisDto.GapAnalysisResponse response = skillGapService.analyzeSkillGap(100L, 1L);

        assertNotNull(response);
        assertEquals(1L, response.getRoleId());
        assertEquals("Full Stack Developer", response.getRoleTitle());
        assertEquals(2, response.getTotalRequiredSkills());
        assertEquals(1, response.getSkillsMetCount());
        assertEquals(1, response.getCriticalGapsCount());
        // Total points required: 4+4=8. Points achieved: min(2,4) + min(4,4) = 2+4=6. Readiness = 6/8 = 75%.
        assertEquals(75, response.getReadinessScore());
        assertEquals(2, response.getSkillGaps().size());
    }
}
