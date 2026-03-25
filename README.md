🚀 SentinelVault-AI

An AI-powered document analysis system that detects sensitive data, classifies risk levels, and enables intelligent search using embeddings (RAG).

⸻

🧠 Project Overview

SentinelVault-AI is a privacy-focused AI system that allows users to upload documents (PDF, TXT, RTF) and automatically:
•	Extract text from files
•	Analyze sensitive data using AI
•	Classify risk levels (LOW / MEDIUM / HIGH)
•	Store embeddings for semantic search
•	Ask questions using RAG (Retrieval-Augmented Generation)

⸻

⚙️ Tech Stack

🔹 Backend
•	Java 22
•	Spring Boot
•	Spring AI
•	MongoDB

🔹 AI & Processing
•	LLM (via Spring AI ChatModel)
•	Embedding Model
•	Apache PDFBox (PDF extraction)
•	Tesseract OCR (fallback for scanned PDFs)

🔹 Automation
•	n8n (workflow automation)

🔄 Architecture

User → n8n Workflow → File Processing → Spring Boot API → AI Analysis → MongoDB

Detailed Flow:

1. File dropped into n8n
2. Switch node routes file type:
    - PDF → PDF extractor
    - TXT/RTF → Text extractor
3. Extracted content sent to Spring Boot
4. AI analyzes content
5. Response parsed safely (with fallback)
6. Embedding generated
7. Data stored in MongoDB
8. RAG used for question answering

📂 Supported File Types
•	✅ PDF (with OCR fallback)
•	✅ TXT
•	✅ RTF

⸻

🔍 Features

📄 Document Analysis
•	Detects:
•	Personal data
•	Financial information
•	Medical data
•	Generates:
•	Summary
•	Sensitive data list
•	Risk level

⸻

🤖 AI Safety Handling
•	Handles broken AI JSON responses
•	Fallback system prevents crashes

⸻

🔎 Semantic Search (RAG)
•	Uses embeddings
•	Finds most relevant documents
•	Answers questions based on stored content

⸻

⚡ Automation (n8n)
•	File ingestion pipeline
•	Smart routing by file type
•	Data transformation before API call

⸻

📡 API Endpoints

🔹 Analyze Document

POST /api/vault/analyze

{
"fileName": "example.pdf",
"content": "text extracted from file"
}

🔹 Ask Question (RAG)

POST /api/vault/ask

{
"question": "What sensitive data is in my documents?"
}

🔹 Search Documents

GET /api/vault/search?query=bank

🗄️ Database Schema (MongoDB)

{
"fileName": "file.pdf",
"content": "...",
"summary": "...",
"sensitiveData": "Email, Phone",
"riskLevel": "MEDIUM",
"embedding": [ ... ],
"createdAt": "timestamp"
}

🛠️ Setup Instructions

1. Clone Repo

git clone https://github.com/your-username/sentinelvault-ai.git
cd sentinelvault-ai

2. Start MongoDB

docker run -d -p 27017:27017 mongo

3. Run Spring Boot

./mvnw spring-boot:run

4. Run n8n

npx n8n

🧪 Example Workflow (n8n)
•	Read file
•	Switch by extension
•	Extract content
•	Send HTTP request to backend

⸻

🚀 Future Improvements
•	Support DOCX files
•	Image analysis (JPG/PNG OCR)
•	Web dashboard UI
•	User authentication
•	Cloud deployment

⸻

👨‍💻 Author

Ranindu Amarasinghe

