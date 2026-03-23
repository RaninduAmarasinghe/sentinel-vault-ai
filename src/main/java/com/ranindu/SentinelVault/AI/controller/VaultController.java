package com.ranindu.SentinelVault.AI.controller;


import com.ranindu.SentinelVault.AI.dto.DocumentRequest;
import com.ranindu.SentinelVault.AI.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vault")
public class VaultController {
private final AiService aiService;
public VaultController(AiService aiService) {
    this.aiService = aiService;
}
@PostMapping("/analyze")
    public String analyze(@RequestBody DocumentRequest request) {
    return aiService.analyzeDocument(request.getContent());
}
}
