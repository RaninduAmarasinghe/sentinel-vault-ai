package com.ranindu.SentinelVault.AI.repository;

import com.ranindu.SentinelVault.AI.entity.DocumentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<DocumentEntity, String> {
}
