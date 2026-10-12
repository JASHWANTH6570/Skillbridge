package com.skillbridge.service;

import com.skillbridge.dto.AiDto;
import com.skillbridge.model.KnowledgeChunk;
import com.skillbridge.repository.KnowledgeChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RagService {

    @Autowired
    private KnowledgeChunkRepository knowledgeChunkRepository;

    /**
     * Performs RAG retrieval over curated, admin-approved knowledge chunks using
     * vector/keyword similarity scoring.
     */
    public List<AiDto.RagSource> retrieveRelevantKnowledge(String query, int topK) {
        List<KnowledgeChunk> allChunks = knowledgeChunkRepository.findByApprovedByAdminTrue();
        if (allChunks.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> queryTokens = tokenize(query);

        List<ScoredChunk> scoredChunks = new ArrayList<>();
        for (KnowledgeChunk chunk : allChunks) {
            double score = computeRelevanceScore(queryTokens, chunk);
            if (score > 0.05) {
                scoredChunks.add(new ScoredChunk(chunk, score));
            }
        }

        scoredChunks.sort((a, b) -> Double.compare(b.score, a.score));

        return scoredChunks.stream()
                .limit(topK)
                .map(sc -> {
                    String snippet = sc.chunk.getContent();
                    if (snippet.length() > 280) {
                        snippet = snippet.substring(0, 277) + "...";
                    }
                    return new AiDto.RagSource(
                            sc.chunk.getTitle(),
                            sc.chunk.getTopic(),
                            snippet,
                            sc.chunk.getSourceName(),
                            sc.chunk.getSourceUrl(),
                            Math.round(sc.score * 100.0) / 100.0
                    );
                })
                .collect(Collectors.toList());
    }

    private double computeRelevanceScore(Set<String> queryTokens, KnowledgeChunk chunk) {
        if (queryTokens.isEmpty()) return 0.0;

        Set<String> titleTokens = tokenize(chunk.getTitle());
        Set<String> topicTokens = tokenize(chunk.getTopic());
        Set<String> keywordsTokens = tokenize(chunk.getKeywords() != null ? chunk.getKeywords() : "");
        Set<String> contentTokens = tokenize(chunk.getContent());

        double titleMatch = countOverlap(queryTokens, titleTokens) * 3.0;
        double topicMatch = countOverlap(queryTokens, topicTokens) * 2.5;
        double keywordsMatch = countOverlap(queryTokens, keywordsTokens) * 2.0;
        double contentMatch = countOverlap(queryTokens, contentTokens) * 1.0;

        double rawScore = titleMatch + topicMatch + keywordsMatch + contentMatch;
        double normalizedScore = rawScore / (queryTokens.size() * 3.0);

        return Math.min(1.0, normalizedScore);
    }

    private double countOverlap(Set<String> queryTokens, Set<String> targetTokens) {
        int matches = 0;
        for (String q : queryTokens) {
            for (String t : targetTokens) {
                if (q.equalsIgnoreCase(t) || t.contains(q) || q.contains(t)) {
                    matches++;
                    break;
                }
            }
        }
        return matches;
    }

    private Set<String> tokenize(String text) {
        if (text == null) return Collections.emptySet();
        String[] words = text.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", " ")
                .split("\\s+");
        Set<String> set = new HashSet<>();
        for (String w : words) {
            if (w.length() > 2 && !STOP_WORDS.contains(w)) {
                set.add(w);
            }
        }
        return set;
    }

    private static final Set<String> STOP_WORDS = Set.of(
            "the", "and", "for", "with", "that", "this", "from", "are", "have", "you",
            "your", "can", "how", "what", "which", "should", "learn", "study", "about"
    );

    private static class ScoredChunk {
        KnowledgeChunk chunk;
        double score;

        ScoredChunk(KnowledgeChunk chunk, double score) {
            this.chunk = chunk;
            this.score = score;
        }
    }
}
