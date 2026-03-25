package com.ranindu.SentinelVault.AI.service;

import com.ranindu.SentinelVault.AI.dto.AnalysisResponse;
import com.ranindu.SentinelVault.AI.entity.DocumentEntity;
import com.ranindu.SentinelVault.AI.repository.DocumentRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.embedding.EmbeddingModel;
import java.util.stream.Collectors;

@Service
public class AiService {

    private final DocumentRepository repository;
    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;

    // Constructor Injection (clean & correct)
    public AiService(DocumentRepository repository,
                     ChatModel chatModel,
                     EmbeddingModel embeddingModel) {
        this.repository = repository;
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
    }

    public AnalysisResponse analyzeDocument(String fileName, String content) {


        if (content == null || content.isBlank()) {
            throw new RuntimeException("Content is empty");
        }


        if (content.length() > 5000) {
            content = content.substring(0, 5000); // better than throwing error
        }

        String prompt = """
You are a highly intelligent data privacy and security classification AI.

Your task is to analyze a document and determine its sensitivity level based on the presence of sensitive human data.

### Classification Rules:

HIGH RISK:
- Any medical or health-related information (diseases, diagnoses, symptoms, treatments, prescriptions, medical history)
- Any financial information (bank accounts, transactions, card numbers, balances)
- Any government-issued identification (NIC, passport, SSN, license numbers)
- Any confidential personal records

MEDIUM RISK:
- Personally identifiable information (name, email, phone number, address, workplace, education details)
- Information that can partially identify a person but is not highly sensitive

LOW RISK:
- General content with no personal, financial, or medical relevance
- Technical, educational, or generic text

### IMPORTANT INSTRUCTIONS:
- Do NOT rely only on keywords — understand the MEANING and CONTEXT
- Any mention of a disease or health condition (no matter what disease) should be treated as HIGH RISK
- Do NOT classify system or technical descriptions as sensitive (e.g., "patient management system")
- If the content contains real personal data, prioritize HIGH or MEDIUM appropriately
- If unsure, choose the HIGHER risk level

### OUTPUT FORMAT (STRICT JSON ONLY — NO EXTRA TEXT):
{
  "summary": "...",
  "sensitiveData": ["..."],
  "riskLevel": "LOW | MEDIUM | HIGH"
}

### DOCUMENT:
%s
""".formatted(content);

        String aiResponse = chatModel.call(prompt);
        System.out.println("RAW AI RESPONSE: " + aiResponse);

        AnalysisResponse response;

        try {
            int start = aiResponse.indexOf("{");
            int end = aiResponse.lastIndexOf("}");
            String cleanJson;

            if (start != -1 && end != -1 && end > start) {
                cleanJson = aiResponse.substring(start, end + 1);
            } else {
                // fallback JSON (prevents crash)
                cleanJson = """
        {
          "summary": "AI failed to return proper JSON",
          "sensitiveData": ["unknown"],
          "riskLevel": "MEDIUM"
        }
        """;
            }

            response = new ObjectMapper().readValue(cleanJson, AnalysisResponse.class);

        } catch (Exception e) {
            e.printStackTrace();

            // safe fallback (never crash)
            response = new AnalysisResponse();
            response.setSummary("AI parsing error");
            response.setSensitiveData(List.of("error"));
            response.setRiskLevel("MEDIUM");
        }
        //embedding
        float[] embeddingArray = embeddingModel.embed(content);

        List<Double> embedding = new ArrayList<>();
        for (float value : embeddingArray) {
            embedding.add((double) value);
        }

        //save
        DocumentEntity doc = new DocumentEntity();
        doc.setFileName(fileName);
        doc.setContent(content);
        doc.setSummary(response.getSummary());
        doc.setSensitiveData(String.join(",", response.getSensitiveData()));
        doc.setRiskLevel(response.getRiskLevel());
        doc.setEmbedding(embedding);
        doc.setCreatedAt(LocalDateTime.now());

        repository.save(doc);

        return response;
    }

    //RAG QUESTION
    public String askQuestion(String question) {

        List<DocumentEntity> docs = repository.findAll();

        String context = docs.stream()
                .map(DocumentEntity::getContent)
                .collect(Collectors.joining("\n"));

        String prompt = """
Answer based on the context below.

Context:
%s

Question:
%s
""".formatted(context, question);

        return chatModel.call(prompt);
    }

    public List<DocumentEntity> getAllDocuments() {
        return repository.findAll();
    }

    public List<DocumentEntity> getByRiskLevel(String level) {
        return repository.findByRiskLevel(level);
    }
}
