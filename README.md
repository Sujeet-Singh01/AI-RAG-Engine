# AI-Powered Codebase RAG Engine 🧠💻

An enterprise-grade Retrieval-Augmented Generation (RAG) system designed to dramatically reduce developer onboarding time by enabling natural language querying of complex, unstructured open-source codebases. 

Built with **Java 17** and **Spring Boot**, this engine ingests raw source code and documentation, chunks the data, and maps it into a **1536-dimensional vector space** using **Spring AI** and **OpenAI**. The embeddings are stored and queried using a local **PostgreSQL** database supercharged with the **PGVector** extension, enabling high-performance cosine similarity searches.

## 🚀 Core Features
*   **Automated Data Ingestion:** REST APIs to dynamically read, parse, and chunk local `.java`, `.md`, and `.txt` files.
*   **Semantic Vector Search:** Bypasses fragile keyword searches in favor of mathematical, context-aware similarity retrieval.
*   **LLM Augmentation:** Appends retrieved codebase context to user prompts before sending them to the LLM, effectively eliminating hallucinations.
*   **Cloud-Ready Architecture:** Containerized using Docker with automated CI/CD pipelines via GitHub Actions (AWS Deployment).

## 🛠️ Tech Stack
*   **Backend:** Java 17, Spring Boot 4.0
*   **AI Orchestration:** Spring AI 2.0, OpenAI API (gpt-4o / text-embedding-ada-002)
*   **Database:** PostgreSQL, PGVector
*   **Infrastructure:** Docker, Docker Compose, GitHub Actions, AWS
