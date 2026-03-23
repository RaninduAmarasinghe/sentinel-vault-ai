package com.ranindu.SentinelVault.AI.service;

import com.ranindu.SentinelVault.AI.dto.AnalysisResponse;
import com.ranindu.SentinelVault.AI.entity.DocumentEntity;
import com.ranindu.SentinelVault.AI.repository.DocumentRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;

@Service
public class AiService {
    @Autowired
    private DocumentRepository repository;
    private final OllamaChatModel chatModel;

    public AiService(OllamaChatModel chatModel, DocumentRepository repository) {
        this.chatModel = chatModel;
        this.repository = repository;
    }
    public AnalysisResponse analyzeDocument(String fileName, String content) {

        if (content.length() > 5000) {
            throw new RuntimeException("Document is too long");
        }

        String prompt = """
You are a local private AI system.

You MUST return ONLY valid JSON.
Do NOT include explanations or extra text.

Format:
{
  "summary": "...",
  "sensitiveData": ["..."],
  "riskLevel": "LOW | MEDIUM | HIGH"
}

Document:
%s
""".formatted(content);

        // 🔥 1. Call AI
        String aiResponse = chatModel.call(prompt);

        // 🔥 2. Parse JSON
        ObjectMapper mapper = new ObjectMapper();
        AnalysisResponse response;

        try {
            String cleanJson;

            if (aiResponse.contains("{") && aiResponse.contains("}")) {
                cleanJson = aiResponse.substring(
                        aiResponse.indexOf("{"),
                        aiResponse.lastIndexOf("}") + 1
                );
            } else {
                throw new RuntimeException("Invalid AI response format: " + aiResponse);
            }

            response = mapper.readValue(cleanJson, AnalysisResponse.class);

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 IMPORTANT for debugging
            throw new RuntimeException("Failed to parse AI response: " + aiResponse);
        }

        // 🔥 3. Save CLEAN data
        DocumentEntity doc = new DocumentEntity();
        doc.setFileName(fileName);
        doc.setContent(content);
        doc.setSummary(response.getSummary());
        doc.setSensitiveData(String.join(",", response.getSensitiveData()));
        doc.setRiskLevel(response.getRiskLevel());
        doc.setCreatedAt(LocalDateTime.now());

        repository.save(doc);

        // 🔥 4. Return structured response
        return response;
    }
}
