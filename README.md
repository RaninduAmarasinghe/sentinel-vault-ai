🚀 SentinelVault-AI

Secure AI-powered document intelligence for sensitive data detection and semantic retrieval.

⸻

🧠 Project Overview

SentinelVault-AI is a privacy-focused AI system that automates document analysis by:
•	Detecting sensitive data (PII, financial, medical)
•	Classifying risk levels (LOW, MEDIUM, HIGH)
•	Generating summaries using LLMs
•	Enabling semantic search with RAG (Retrieval-Augmented Generation)

⸻

⚙️ Technical Stack

Backend
•	Java 22
•	Spring Boot
•	Spring AI

Database
•	MongoDB (Document + Vector Storage)

AI & Processing
•	LLM (Spring AI ChatModel)
•	Embeddings for semantic search
•	Apache PDFBox (PDF extraction)
•	Tesseract OCR (scanned documents)

Automation
•	n8n (workflow orchestration)

⸻

🔄 Architecture & Flow
1.	Files are detected via n8n workflow
2.	File types are routed (PDF, TXT, RTF)
3.	Content is extracted (OCR fallback supported)
4.	Data is sent to Spring Boot API
5.	AI analyzes and classifies risk
6.	Embeddings are generated
7.	Data is stored in MongoDB
8.	RAG enables intelligent querying

⸻

🔍 Key Features

📄 Document Analysis
•	Detects sensitive data (NIC, Email, Phone, Bank details)
•	Generates summaries
•	Classifies risk levels

🤖 AI Safety Handling
•	Handles malformed AI responses
•	Uses fallback parsing logic

🔎 Semantic Search (RAG)
•	Context-aware document retrieval
•	Ask questions like:
“Which documents contain my bank details?”

🔄 Automation (n8n)
•	Folder monitoring
•	File-type routing
•	API integration pipeline

⸻

📂 Supported File Types
•	PDF (with OCR fallback)
•	TXT
•	RTF

⸻

📡 API Endpoints

1. Analyze Document

POST /api/vault/analyze

{
"fileName": "example.pdf",
"content": "extracted text"
}


⸻

2. Ask Question (RAG)

POST /api/vault/ask

{
"question": "What sensitive data is in my documents?"
}


⸻

🔄 n8n Workflow

The system includes an automated workflow:
•	Monitors folders for new files
•	Extracts content
•	Sends data to backend
•	Triggers alerts for high-risk data

📂 Workflow file:
/n8n-workflow/sentinelvault-workflow.json

⸻

🛠️ Setup

1. Clone repository

git clone https://github.com/RaninduAmarasinghe/sentinel-vault-ai.git
cd sentinel-vault-ai

2. Start MongoDB

docker run -d -p 27017:27017 mongo

3. Run backend

./mvnw spring-boot:run

4. Run n8n

npx n8n


⸻

🚀 Future Improvements
•	DOCX support
•	Image OCR (JPG/PNG)
•	Web dashboard (React)
•	Authentication (JWT/OAuth)

⸻

👨‍💻 Author

Ranindu Amarasinghe