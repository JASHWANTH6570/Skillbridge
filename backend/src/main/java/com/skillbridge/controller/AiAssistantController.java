package com.skillbridge.controller;

import com.skillbridge.dto.AiDto;
import com.skillbridge.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiAssistantController {

    @Autowired
    private AiAssistantService aiAssistantService;

    @PostMapping("/chat")
    public ResponseEntity<AiDto.ChatResponse> chatWithAssistant(@RequestBody AiDto.ChatRequest request) {
        AiDto.ChatResponse response = aiAssistantService.generateGuidance(request);
        return ResponseEntity.ok(response);
    }
}
