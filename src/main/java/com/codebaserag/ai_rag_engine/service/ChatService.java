package com.codebaserag.ai_rag_engine.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    public ChatService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();
        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        this.chatClient = builder
                .defaultAdvisors(memoryAdvisor)
                .build();

    }
    public String askQuestion(String userQuery) {
        // 1. Retrieve the most relevant code chunks from PGVector
        // We use similarity search, looking for the top 4 closest matches
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuery)
                        .topK(4)
                        .build()
        );

        // 2. Combine the retrieved code chunks into a single string
        String codebaseContext = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        return chatClient.prompt()
                .system(sys -> sys.text("""
                        You are a senior Java architect assistant. Use the following retrieved codebase context to answer the user's question.
                        If you don't know the answer based on the context, just say that you don't know. Don't make up code.
                        
                        Codebase Context:
                        {context}
                        """)
                        .param("context", codebaseContext))
                .user(userQuery)
                .advisors((a-> a.param(ChatMemory.CONVERSATION_ID, "default-user-session")))
                .call()
                .content();
    }

}
