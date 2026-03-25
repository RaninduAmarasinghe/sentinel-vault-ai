package com.ranindu.SentinelVault.AI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResult {

    private String fileName;
    private double similarity;
    private String content;

}
