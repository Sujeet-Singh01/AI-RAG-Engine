package com.codebaserag.ai_rag_engine.service;


import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;

@Service
public class IngestionService {
    private final VectorStore vectorStore;
    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingestLocalFile(String absoluteFilePath) {
        FileSystemResource resource = new FileSystemResource(new File(absoluteFilePath));
        TextReader textReader = new TextReader(resource);

        textReader.getCustomMetadata().put("filename", resource.getFilename());
        List<Document> rawDocuments = textReader.get();

        TokenTextSplitter textSplitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .build();

        List<Document> splitDocuments = textSplitter.apply(rawDocuments);
        vectorStore.add(splitDocuments);

    }
}
