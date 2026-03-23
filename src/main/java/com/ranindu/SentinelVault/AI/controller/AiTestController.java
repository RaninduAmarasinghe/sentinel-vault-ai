package com.ranindu.SentinelVault.AI.controller;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiTestController {
    private final OllamaChatModel chatModel;

    public AiTestController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }
    @GetMapping("/test-ai")
    public String generate(@RequestParam(value = "message", defaultValue = "Hi! Are you running locally?") String message) {
        return chatModel.call(message);
    }
}
