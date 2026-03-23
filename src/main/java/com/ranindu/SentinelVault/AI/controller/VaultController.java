package com.ranindu.SentinelVault.AI.controller;


import com.ranindu.SentinelVault.AI.dto.DocumentRequest;
import com.ranindu.SentinelVault.AI.service.AiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        return aiService.analyzeDocument(content);
    }
}
