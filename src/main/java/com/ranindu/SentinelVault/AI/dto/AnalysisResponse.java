package com.ranindu.SentinelVault.AI.dto;

import lombok.Data;

@Data
public class AnalysisResponse {
    private String summary;
    private String sensitiveData;
    private String riskLevel;
}
