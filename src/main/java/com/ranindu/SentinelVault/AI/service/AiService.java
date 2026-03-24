package com.ranindu.SentinelVault.AI.service;

import com.ranindu.SentinelVault.AI.dto.AnalysisResponse;
import com.ranindu.SentinelVault.AI.entity.DocumentEntity;
import com.ranindu.SentinelVault.AI.repository.DocumentRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;

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

        // 🔥 1. Call AI
        String aiResponse = chatModel.call(prompt);

        // 🔥 2. Parse JSON
        ObjectMapper mapper = new ObjectMapper();
        AnalysisResponse response;

        try {
            String cleanJson;

            int start = aiResponse.indexOf("{");
            int end = aiResponse.lastIndexOf("}");

            if (start != -1 && end != -1 && end > start) {
                cleanJson = aiResponse.substring(start, end + 1);
            } else {
                // 🔥 fallback: try fixing missing closing brace
                cleanJson = aiResponse.trim();
                if (!cleanJson.endsWith("}")) {
                    cleanJson = cleanJson + "}";
                }
            }

            System.out.println("CLEAN JSON: " + cleanJson); // debug

            response = mapper.readValue(cleanJson, AnalysisResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
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
    //History
    public List<DocumentEntity> getAllDocuments() {
        return repository.findAll();
    }
    //Filter
    public List<DocumentEntity> getByRiskLevel(String level) {
        return repository.findByRiskLevel(level);
    }
}
