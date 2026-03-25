🚀 SentinelVault-AI

SentinelVault-AI is an AI-powered document analysis system that detects sensitive data, classifies risk levels, and enables intelligent search using RAG (Retrieval-Augmented Generation).

⸻

🧠 Project Overview

SentinelVault-AI is a privacy-focused system that allows users to upload documents and automatically:
	•	Extract text from files
	•	Detect sensitive data using AI
	•	Classify risk levels (LOW / MEDIUM / HIGH)
	•	Generate embeddings for semantic search
	•	Answer questions using RAG

⸻

⚙️ Tech Stack

Backend
	•	Java 22
	•	Spring Boot
	•	Spring AI
	•	MongoDB

AI & Processing
	•	LLM (Spring AI ChatModel)
	•	Embedding Model
	•	Apache PDFBox (PDF extraction)
	•	Tesseract OCR (for scanned PDFs)

Automation
	•	n8n

⸻

🔄 Architecture

User → n8n Workflow → File Processing → Spring Boot API → AI Analysis → MongoDB

⸻

🔁 Processing Flow
	1.	File is uploaded to n8n
	2.	File type is detected:
	•	PDF → PDF extractor
	•	TXT / RTF → Text extractor
	3.	Extracted content is sent to Spring Boot
	4.	AI analyzes the content
	5.	Response is parsed safely (fallback included)
	6.	Embedding is generated
	7.	Data is stored in MongoDB
	8.	RAG is used for question answering

⸻

📂 Supported File Types
	•	PDF (with OCR fallback)
	•	TXT
	•	RTF

⸻

🔍 Features

Document Analysis
	•	Detects personal, financial, and medical data
	•	Generates summary and sensitive data list
	•	Classifies risk level

AI Safety Handling
	•	Handles invalid AI JSON responses
	•	Prevents system crashes using fallback logic

Semantic Search (RAG)
	•	Uses embeddings
	•	Retrieves relevant documents
	•	Answers user questions

Automation (n8n)
	•	File ingestion pipeline
	•	Smart routing by file type
	•	Data transformation before API calls

⸻

📡 API Endpoints

Analyze Document

POST /api/vault/analyze

{
“fileName”: “example.pdf”,
“content”: “text extracted from file”
}

⸻

Ask Question (RAG)

POST /api/vault/ask

{
“question”: “What sensitive data is in my documents?”
}

⸻

Search Documents

GET /api/vault/search?query=bank

⸻

🗄️ Database Schema

{
“fileName”: “file.pdf”,
“content”: “…”,
“summary”: “…”,
“sensitiveData”: “Email, Phone”,
“riskLevel”: “MEDIUM”,
“embedding”: [ … ],
“createdAt”: “timestamp”
}

⸻

🛠️ Setup Instructions
	1.	Clone repository
git clone https://github.com/your-username/sentinelvault-ai.git
cd sentinelvault-ai
	2.	Start MongoDB
docker run -d -p 27017:27017 mongo
	3.	Run Spring Boot
./mvnw spring-boot:run
	4.	Run n8n
npx n8n

⸻

🧪 Example n8n Workflow
	•	Read file
	•	Switch by file type
	•	Extract content
	•	Send HTTP request to backend

⸻

🚀 Future Improvements
	•	DOCX support
	•	Image analysis (JPG/PNG OCR)
	•	Web dashboard UI
	•	User authentication
	•	Cloud deployment

⸻

👨‍💻 Author

Ranindu Amarasinghe
