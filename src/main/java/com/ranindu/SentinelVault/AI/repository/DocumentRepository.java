package com.ranindu.SentinelVault.AI.repository;

import com.ranindu.SentinelVault.AI.entity.DocumentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentRepository extends MongoRepository<DocumentEntity, String> {
    List<DocumentEntity> findByRiskLevel(String riskLevel);
    boolean existsByFileName(String fileName);
}
