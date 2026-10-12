package com.skillbridge.repository;

import com.skillbridge.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByRoadmapIdOrderByOrderIndexAsc(Long roadmapId);
}
