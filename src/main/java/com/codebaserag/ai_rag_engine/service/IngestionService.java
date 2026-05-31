package com.codebaserag.ai_rag_engine.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


@Service
public class IngestionService {
   /* private final VectorStore vectorStore;
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

    }*/

    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);
    private final VectorStore vectorStore;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".java", ".xml", ".yml", ".yaml", ".properties", ".md", ".txt");
    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public String ingestionDirectory(String directoryPath) throws IOException {
        Path startPath = Paths.get(directoryPath);
        if(!Files.exists(startPath) || !Files.isDirectory(startPath)) {
            throw new IllegalArgumentException("Invalid directory path provided: " + directoryPath);
        }

        List<Document> allDocuments = new ArrayList<>();
        //traversing the directory recursively
        try (Stream<Path> paths = Files.walk(startPath)) {
            List<Path> validFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(this::isAllowedFileExtension)
                    .toList();
            log.info("Found valid files to ingest. " + validFiles.size());

            // read each file and attach metadata
            for(Path file : validFiles) {
                FileSystemResource resource = new FileSystemResource(file);
                TextReader textReader = new TextReader(resource);

                //tell AI which file this code came from

                textReader.getCustomMetadata().put("filename", file.getFileName().toString());
                textReader.getCustomMetadata().put("filepath", file.toAbsolutePath().toString());
                allDocuments.addAll(textReader.read());

            }
        }
        catch (IOException e) {
            log.error("Failed to read directory", e);
            return "Error reading directory: " + e.getMessage();
        }
        //chunk the files into smaller pieces to fit into the AI's context window
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .build();
        List<Document> chunkedDocuments = splitter.split(allDocuments);

        log.info("Split files into chunks. saving to PGVector....", chunkedDocuments.size());

        //save to database using Ollama for embeddings
        vectorStore.add(chunkedDocuments);

        return String.format("Successfully ingested %d files (split into %d chunks) into the vector store.",
                allDocuments.size(), chunkedDocuments.size());
    }
    private boolean isAllowedFileExtension(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        return ALLOWED_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }
}
