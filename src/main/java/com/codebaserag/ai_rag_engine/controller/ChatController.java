package com.codebaserag.ai_rag_engine.controller;

import com.codebaserag.ai_rag_engine.service.ChatService;
import com.codebaserag.ai_rag_engine.service.IngestionService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/api/chat")
    public String chat(@RequestParam String query) {
        return chatService.askQuestion(query);
    }


}
