package com.ranindu.SentinelVault.AI.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnalysisResponse {
    private String summary;
    private List<String> sensitiveData;;
    private String riskLevel;
}
