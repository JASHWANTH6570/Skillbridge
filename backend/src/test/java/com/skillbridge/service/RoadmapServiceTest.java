package com.skillbridge.service;

import com.skillbridge.dto.GapAnalysisDto;
import com.skillbridge.dto.RoadmapDto;
import com.skillbridge.model.*;
import com.skillbridge.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoadmapServiceTest {

    @Mock
    private RoadmapRepository roadmapRepository;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private RoleProfileRepository roleProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private SkillGapService skillGapService;

    @InjectMocks
    private RoadmapService roadmapService;

    @Test
    void testToggleMilestoneCompletion_Success() {
        User user = new User("testuser", "test@test.com", "pass", "Test", RoleType.ROLE_STUDENT);
        user.setId(1L);

        RoleProfile role = new RoleProfile("Full Stack", "Eng", "Desc", "Entry");
        role.setId(2L);

        Roadmap roadmap = new Roadmap(user, role, "Test Roadmap", "Summary");
        roadmap.setId(10L);
        roadmap.setTotalMilestones(2);
        roadmap.setCompletedMilestones(0);

        Milestone milestone = new Milestone(roadmap, 1, 1, "Milestone 1", "Desc", "Java", "Project", "Outcome", "Links");
        milestone.setId(100L);
        milestone.setCompleted(false);

        when(milestoneRepository.findById(100L)).thenReturn(Optional.of(milestone));
        when(milestoneRepository.save(any(Milestone.class))).thenReturn(milestone);
        when(milestoneRepository.findByRoadmapIdOrderByOrderIndexAsc(10L)).thenReturn(List.of(milestone));
        when(roadmapRepository.save(any(Roadmap.class))).thenReturn(roadmap);

        RoadmapDto.RoadmapResponse response = roadmapService.toggleMilestoneCompletion(1L, 100L);

        assertNotNull(response);
        assertTrue(milestone.isCompleted());
        verify(milestoneRepository, times(1)).save(milestone);
        verify(roadmapRepository, times(1)).save(roadmap);
    }
}
