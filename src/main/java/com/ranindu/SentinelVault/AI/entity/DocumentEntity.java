package com.ranindu.SentinelVault.AI.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "documnets")
public class DocumentEntity {
    @Id
    private String id;
    private String fileName;
    private String content;
    private String summary;
    private String riskLevel;
    private LocalDateTime createdAt;
}
