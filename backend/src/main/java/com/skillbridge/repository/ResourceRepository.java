package com.skillbridge.repository;

import com.skillbridge.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findBySkillNameIgnoreCase(String skillName);
    List<Resource> findByType(String type);
}
