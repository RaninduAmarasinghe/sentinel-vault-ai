package com.ranindu.SentinelVault.AI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RagResponse {
    private String answer;
    private String sourse;
    private double confidence;
}
