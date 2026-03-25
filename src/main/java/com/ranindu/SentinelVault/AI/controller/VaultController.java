package com.ranindu.SentinelVault.AI.controller;


import com.ranindu.SentinelVault.AI.dto.AnalysisResponse;
import com.ranindu.SentinelVault.AI.dto.DocumentRequest;
import com.ranindu.SentinelVault.AI.entity.DocumentEntity;
import com.ranindu.SentinelVault.AI.service.AiService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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

    System.out.println("FILE: " + request.getFileName());
    System.out.println("CONTENT: " + request.getContent());

    return aiService.analyzeDocument(
            request.getFileName(),
            request.getContent()
    );
}
//Upload file
    @PostMapping("/upload")
    public AnalysisResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
      String fileName = file.getOriginalFilename();
      String content;

      if(fileName != null && fileName.endsWith(".pdf")){
          //extract document
          try(PDDocument document = PDDocument.load(file.getInputStream())){
              PDFTextStripper stripper = new PDFTextStripper();
              content = stripper.getText(document);
          }
      }else {
          content = new String(file.getBytes());
      }
      return aiService.analyzeDocument(fileName, content);
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

    //RAG endpoint
    @PostMapping("/ask")
    public String ask(@RequestBody String question) {
    return aiService.askQuestion(question);
    }
}
