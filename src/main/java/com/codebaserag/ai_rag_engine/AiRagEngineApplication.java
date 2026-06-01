package com.codebaserag.ai_rag_engine;


import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiRagEngineApplication {

	public static void main(String[] args) {

		SpringApplication.run(AiRagEngineApplication.class, args);
	}

}
