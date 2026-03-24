package com.ranindu.SentinelVault.AI.controller;


import com.ranindu.SentinelVault.AI.dto.AnalysisResponse;
import com.ranindu.SentinelVault.AI.dto.DocumentRequest;
import com.ranindu.SentinelVault.AI.entity.DocumentEntity;
import com.ranindu.SentinelVault.AI.service.AiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/vault")
public class VaultController {

private final AiService aiService;

public VaultController(AiService aiService) {
    this.aiService = aiService;
}

//Analyze JSON input
    @PostMapping("/analyze")
    public AnalysisResponse analyze(@RequestBody DocumentRequest request) {
        return aiService.analyzeDocument(
                request.getFileName(),
                request.getContent()
        );
    }
//Upload file
    @PostMapping("/upload")
    public AnalysisResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes());

        return aiService.analyzeDocument(
                file.getOriginalFilename(),
                content
        );
    }

// Get history
    @GetMapping("/history")
    public List<DocumentEntity> getHistory() {
    return aiService.getAllDocuments();
    }

//Filter by risk
    @GetMapping("/history/risk")
    public List<DocumentEntity> getByRisk(@RequestParam String level) {
    return aiService.getByRiskLevel(level);
    }

}
