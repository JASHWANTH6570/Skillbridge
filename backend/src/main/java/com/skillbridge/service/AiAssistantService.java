package com.skillbridge.service;

import com.skillbridge.dto.AiDto;
import com.skillbridge.model.RoleProfile;
import com.skillbridge.repository.RoleProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AiAssistantService {

    @Autowired
    private RagService ragService;

    @Autowired
    private RoleProfileRepository roleProfileRepository;

    public AiDto.ChatResponse generateGuidance(AiDto.ChatRequest request) {
        String query = request.getMessage() != null ? request.getMessage().trim() : "";
        List<AiDto.RagSource> sources = ragService.retrieveRelevantKnowledge(query, 3);

        String roleContextTitle = "";
        if (request.getRoleId() != null) {
            Optional<RoleProfile> roleOpt = roleProfileRepository.findById(request.getRoleId());
            if (roleOpt.isPresent()) {
                roleContextTitle = roleOpt.get().getTitle();
            }
        }

        StringBuilder answerBuilder = new StringBuilder();
        StringBuilder reasonBuilder = new StringBuilder();
        List<String> nextSteps = new ArrayList<>();

        if (sources.isEmpty()) {
            answerBuilder.append("Based on curated career pathways, for ")
                    .append(!roleContextTitle.isEmpty() ? roleContextTitle : "your target role")
                    .append(", we recommend focusing on core architectural principles, hands-on production code, and clear milestone verification.");
            reasonBuilder.append("Establishing systematic fundamentals before taking on specialized tools dramatically reduces cognitive overload and improves interview readiness.");
            nextSteps.add("Review the primary role requirements under your roadmap overview.");
            nextSteps.add("Break down large learning topics into discrete weekly milestones.");
            nextSteps.add("Build a dedicated GitHub repository showing practical commits for each milestone.");
        } else {
            AiDto.RagSource topSource = sources.get(0);
            answerBuilder.append("According to curated industry guidance on ").append(topSource.getTopic()).append(":\n\n")
                    .append(topSource.getSnippet()).append("\n\n");

            if (sources.size() > 1) {
                answerBuilder.append("Additionally, ").append(sources.get(1).getSourceName())
                        .append(" emphasizes mastering practical workflows: ")
                        .append(sources.get(1).getSnippet());
            }

            reasonBuilder.append("This recommendation is grounded directly in verified curriculum standards from ")
                    .append(topSource.getSourceName())
                    .append(" and aligns with hiring expectations for modern software and engineering roles.");

            nextSteps.add("Consult the approved resource: " + topSource.getTitle() + " at " + topSource.getSourceUrl());
            nextSteps.add("Implement a practical exercise or module verifying your understanding of " + topSource.getTopic() + ".");
            nextSteps.add("Mark the corresponding milestone completed on your SkillBridge roadmap to update your career readiness score.");
        }

        return new AiDto.ChatResponse(
                answerBuilder.toString(),
                reasonBuilder.toString(),
                nextSteps,
                sources
        );
    }
}
