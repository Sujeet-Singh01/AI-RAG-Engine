package com.codebaserag.ai_rag_engine.controller;

import com.codebaserag.ai_rag_engine.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
