package com.skillbridge.repository;

import com.skillbridge.model.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {
    List<StudentSkill> findByUserId(Long userId);
    Optional<StudentSkill> findByUserIdAndSkillId(Long userId, Long skillId);
    void deleteByUserId(Long userId);
}
