package com.skillbridge.repository;

import com.skillbridge.model.RoleProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleProfileRepository extends JpaRepository<RoleProfile, Long> {
    Optional<RoleProfile> findByTitle(String title);
}
