package com.ranindu.SentinelVault.AI.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "documnets")
@Data
public class DocumentEntity {
    @Id
    private String id;
    private String fileName;
    private String content;
    private String summary;
    private String sensitiveData;
    private String riskLevel;
    private List<Double> embedding;
    private LocalDateTime createdAt;
}
