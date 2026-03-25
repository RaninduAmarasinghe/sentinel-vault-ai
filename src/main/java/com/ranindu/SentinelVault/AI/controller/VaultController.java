package com.ranindu.SentinelVault.AI.controller;


import com.ranindu.SentinelVault.AI.dto.*;
import com.ranindu.SentinelVault.AI.entity.DocumentEntity;
import com.ranindu.SentinelVault.AI.service.AiService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
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
public AnalysisResponse analyze(@RequestBody AnalysisRequest request) {

    System.out.println("CONTROLLER FILE: [" + request.getFileName() + "]"); // 🔥 DEBUG
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
    String safeName = fileName != null ? fileName : "unknown.txt";
    String lowerName = safeName.toLowerCase();

    String content;

    if (lowerName.endsWith(".pdf")) {

        //PDF
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            content = stripper.getText(document);
        }

    } else if (lowerName.endsWith(".rtf")) {

        //RTF
        content = extractTextFromRTF(file);

    } else {

        //  TXT / OTHER
        content = new String(file.getBytes());
    }

    return aiService.analyzeDocument(safeName, content);
}


    //RTF EXTRACTOR (NEW)

    private String extractTextFromRTF(MultipartFile file) {
        try {
            RTFEditorKit rtfParser = new RTFEditorKit();
            Document document = rtfParser.createDefaultDocument();

            try (var inputStream = file.getInputStream()) {
                rtfParser.read(inputStream, document, 0);
            }

            return document.getText(0, document.getLength());

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse RTF file", e);
        }
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
    public RagResponse ask(@RequestBody QuestionRequest request) {
        return aiService.askQuestion(request.getQuestion());
    }

    //Search
    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam String query) {
    return aiService.search(query);
    }
}
