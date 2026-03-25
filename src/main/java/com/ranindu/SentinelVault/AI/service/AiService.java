package com.ranindu.SentinelVault.AI.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranindu.SentinelVault.AI.dto.AnalysisResponse;
import com.ranindu.SentinelVault.AI.dto.RagResponse;
import com.ranindu.SentinelVault.AI.dto.SearchResult;
import com.ranindu.SentinelVault.AI.entity.DocumentEntity;
import com.ranindu.SentinelVault.AI.repository.DocumentRepository;
import com.ranindu.SentinelVault.AI.util.VectorUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiService {

    private final DocumentRepository repository;
    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;

    public AiService(DocumentRepository repository,
                     ChatModel chatModel,
                     EmbeddingModel embeddingModel) {
        this.repository = repository;
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
    }

    //ANALYZE DOCUMENT
    public AnalysisResponse analyzeDocument(String fileName, String content) {

        System.out.println("SERVICE RECEIVED FILE: [" + fileName + "]");

        if (content == null || content.isBlank()) {
            throw new RuntimeException("Content is empty");
        }

        //CLEAN TEXT
        content = cleanText(content);

        // LIMIT SIZE
        if (content.length() > 5000) {
            content = content.substring(0, 5000);
        }

        //FIX: handle null + empty filename
        if (fileName == null || fileName.isBlank()) {
            fileName = "unknown.txt";
        }

        // PROMPT
        String prompt = """
You are a highly intelligent data privacy and security classification AI.

Your task is to analyze a document and determine its sensitivity level.

Rules:
- Medical data → HIGH
- Financial data → HIGH
- IDs → HIGH
- Personal info → MEDIUM
- General text → LOW
- If unsure → choose HIGHER

Return ONLY JSON:

{
  "summary": "...",
  "sensitiveData": ["..."],
  "riskLevel": "LOW | MEDIUM | HIGH"
}

DOCUMENT:
%s
""".formatted(content);

        String aiResponse = chatModel.call(prompt);
        System.out.println("RAW AI RESPONSE: " + aiResponse);

        AnalysisResponse response;

        try {
            // Extract JSON
            String cleanJson = aiResponse.replaceAll("(?s).*?(\\{.*)", "$1");

            // Fix broken JSON (missing braces)
            int openBraces = cleanJson.length() - cleanJson.replace("{", "").length();
            int closeBraces = cleanJson.length() - cleanJson.replace("}", "").length();

            while (closeBraces < openBraces) {
                cleanJson += "}";
                closeBraces++;
            }

            System.out.println("FIXED JSON: " + cleanJson);

            // SAFE PARSE
            try {
                response = new ObjectMapper().readValue(cleanJson, AnalysisResponse.class);

            } catch (Exception e) {
                System.out.println("⚠️ JSON PARSE FAILED → using fallback");

                response = new AnalysisResponse();
                response.setSummary("Fallback summary");
                response.setSensitiveData(List.of("unknown"));
                response.setRiskLevel("MEDIUM");
            }

        } catch (Exception e) {
            e.printStackTrace();

            response = new AnalysisResponse();
            response.setSummary("Critical parsing error");
            response.setSensitiveData(List.of("error"));
            response.setRiskLevel("HIGH");
        }

        //EMBEDDING
        float[] embeddingArray = embeddingModel.embed(content);

        List<Double> embedding = new ArrayList<>();
        for (float value : embeddingArray) {
            embedding.add((double) value);
        }

        // SAVE
        DocumentEntity doc = new DocumentEntity();
        doc.setFileName(fileName);
        doc.setContent(content);
        doc.setSummary(response.getSummary());

        // NULL SAFE
        doc.setSensitiveData(
                response.getSensitiveData() != null
                        ? String.join(",", response.getSensitiveData())
                        : ""
        );

        doc.setRiskLevel(response.getRiskLevel());
        doc.setEmbedding(embedding);
        doc.setCreatedAt(LocalDateTime.now());

        repository.save(doc);

        return response;
    }
    //  RAG QUESTION
    public RagResponse askQuestion(String question) {

        float[] queryArray = embeddingModel.embed(question);

        List<Double> queryEmbedding = new ArrayList<>();
        for (float v : queryArray) {
            queryEmbedding.add((double) v);
        }

        List<DocumentEntity> docs = repository.findAll();

        if (docs.isEmpty()) {
            return new RagResponse("No documents found", "none", 0.0);
        }

        //  Find best matching doc
        DocumentEntity bestDoc = docs.stream()
                .filter(doc -> doc.getEmbedding() != null)
                .max((d1, d2) -> {
                    double sim1 = VectorUtils.cosineSimilarity(queryEmbedding, d1.getEmbedding());
                    double sim2 = VectorUtils.cosineSimilarity(queryEmbedding, d2.getEmbedding());
                    return Double.compare(sim1, sim2);
                })
                .orElse(null);

        double confidence = VectorUtils.cosineSimilarity(queryEmbedding, bestDoc.getEmbedding());

        String prompt = """
You are an AI assistant.

Answer ONLY using the context below.
If not found, say "Not found".

Context:
%s

Question:
%s
""".formatted(bestDoc.getContent(), question);

        String answer = chatModel.call(prompt);

        return new RagResponse(
                answer,
                bestDoc.getFileName(),
                confidence
        );
    }

    // SEARCH
    public List<SearchResult> search(String query) {

        float[] queryArray = embeddingModel.embed(query);

        List<Double> queryEmbedding = new ArrayList<>();
        for (float v : queryArray) {
            queryEmbedding.add((double) v);
        }

        List<DocumentEntity> docs = repository.findAll();

        return docs.stream()
                .filter(doc -> doc.getEmbedding() != null)
                .map(doc -> {
                    double sim = VectorUtils.cosineSimilarity(queryEmbedding, doc.getEmbedding());
                    return new SearchResult(
                            doc.getFileName(),
                            sim,
                            doc.getContent()
                    );
                })
                .sorted((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()))
                .limit(5)
                .toList();
    }

    // HISTORY
    public List<DocumentEntity> getAllDocuments() {
        return repository.findAll();
    }

    public List<DocumentEntity> getByRiskLevel(String level) {
        return repository.findByRiskLevel(level);
    }

    // CLEAN TEXT
    private String cleanText(String text) {
        return text
                .replaceAll("\\\\[a-zA-Z]+\\d*", "")
                .replaceAll("\\{.*?\\}", "")
                .replaceAll("(?i)helvetica|arial|timesnewroman|courier", "")
                .replaceAll("(?i)ddirnatural|tightenfactor|cocoartf\\d*", "")
                .replaceAll("[^a-zA-Z0-9.,:;\\-\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}