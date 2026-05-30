package com.codebaserag.ai_rag_engine.controller;

import com.codebaserag.ai_rag_engine.service.IngestionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngestionController {
    private final IngestionService ingestionService;
    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }
    @PostMapping("/api/ingest")
    public String ingestFile(@RequestParam String filePath) {
        try {
            ingestionService.ingestLocalFile(filePath);
            return "Successfully ingested, embedded, and saved: " + filePath;
        }catch (Exception e) {
            return "failed to ingest file. Error: " + e.getMessage();
        }
    }
}
