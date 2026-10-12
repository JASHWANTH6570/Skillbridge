package com.skillbridge.repository;

import com.skillbridge.model.KnowledgeChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeChunkRepository extends JpaRepository<KnowledgeChunk, Long> {
    List<KnowledgeChunk> findByApprovedByAdminTrue();

    @Query("SELECT k FROM KnowledgeChunk k WHERE k.approvedByAdmin = true AND " +
           "(LOWER(k.topic) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(k.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(k.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(k.keywords) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<KnowledgeChunk> searchApprovedKnowledge(@Param("query") String query);
}
