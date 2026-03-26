🚀 SentinelVault-AI
Secure AI-powered document intelligence for sensitive data detection and semantic retrieval.

<<<<<<< HEAD
SentinelVault-AI is an AI-powered document analysis system that detects sensitive data, classifies risk levels, and enables intelligent search using RAG (Retrieval-Augmented Generation).

⸻
=======
SentinelVault-AI is a privacy-centric system designed to automate document analysis. It bridges the gap between raw unstructured data and actionable insights by extracting text, identifying PII (Personally Identifiable Information), classifying risk, and enabling intelligent RAG-based (Retrieval-Augmented Generation) querying.
>>>>>>> f336789 (fix: prevent duplicate documents from being stored in database)

🧠 Project Overview
Modern organizations handle massive amounts of sensitive data across various file formats. SentinelVault-AI automates the labor-intensive task of manual auditing by using LLMs to:

<<<<<<< HEAD
SentinelVault-AI is a privacy-focused system that allows users to upload documents and automatically:
	•	Extract text from files
	•	Detect sensitive data using AI
	•	Classify risk levels (LOW / MEDIUM / HIGH)
	•	Generate embeddings for semantic search
	•	Answer questions using RAG
=======
Audit: Automatically detect personal, financial, and medical data.
>>>>>>> f336789 (fix: prevent duplicate documents from being stored in database)

Classify: Assign risk levels (LOW, MEDIUM, HIGH) based on content.

Retrieve: Use vector embeddings for semantic "Ask your Data" capabilities.

<<<<<<< HEAD
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
=======
⚙️ Technical Stack
Component	Technology
Backend	Java 22, Spring Boot 3.x, Spring AI
Database	MongoDB (Document Store & Vector Search)
AI Models	LLM (via Spring AI ChatModel), Vector Embeddings
Processing	Apache PDFBox, Tesseract OCR
Automation	n8n (Workflow Orchestration)
🔄 Architecture & Data Flow
The system follows a modular pipeline designed for scalability and reliability:

Ingestion: Files are dropped into an n8n workflow.

Routing: A Switch node routes files based on extension (.pdf, .txt, .rtf).
>>>>>>> f336789 (fix: prevent duplicate documents from being stored in database)

Extraction: Text is extracted (using OCR fallback for scanned images).

Processing: The Spring Boot API receives content and coordinates with the LLM.

<<<<<<< HEAD
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
=======
Analysis: The AI generates a summary, identifies sensitive entities, and calculates risk.

Persistence: Data is vectorized and stored in MongoDB for both metadata and semantic search.

🔍 Key Features
📄 Intelligent Document Analysis
>>>>>>> f336789 (fix: prevent duplicate documents from being stored in database)

PII Detection: Identifies Names, SSNs, Credit Card numbers, and Medical IDs.

Risk Scoring: Categorizes documents to prioritize security reviews.

<<<<<<< HEAD
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
=======
Summarization: Provides concise overviews of long-form documents.

🤖 Resilience & Safety

JSON Repair: Custom logic to handle and parse "hallucinated" or malformed AI responses.

Fallback OCR: Switches to Tesseract if PDFBox encounters image-only PDF pages.

🔎 Semantic Search (RAG)

Goes beyond keyword matching to find documents based on intent.

Answers complex questions (e.g., "Which documents contain my bank details?") using context-aware retrieval.

📡 API Reference
1. Analyze Document
>>>>>>> f336789 (fix: prevent duplicate documents from being stored in database)

POST /api/vault/analyze

JSON
{
<<<<<<< HEAD
“fileName”: “example.pdf”,
“content”: “text extracted from file”
}

⸻

Ask Question (RAG)
=======
"fileName": "tax_return_2025.pdf",
"content": "Raw extracted text here..."
}
2. Ask Question (RAG)
>>>>>>> f336789 (fix: prevent duplicate documents from being stored in database)

POST /api/vault/ask

JSON
{
<<<<<<< HEAD
“question”: “What sensitive data is in my documents?”
=======
"question": "What is the total risk exposure across my uploaded files?"
>>>>>>> f336789 (fix: prevent duplicate documents from being stored in database)
}
🛠️ Getting Started
Prerequisites

<<<<<<< HEAD
⸻

Search Documents
=======
JDK 22
>>>>>>> f336789 (fix: prevent duplicate documents from being stored in database)

Docker (for MongoDB)

<<<<<<< HEAD
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
=======
Node.js (for n8n)

Installation

Clone the repository:

Bash
git clone https://github.com/your-username/sentinelvault-ai.git
Launch Infrastructure:

Bash
docker run -d -p 27017:27017 --name sentinel-mongo mongo
Start the Backend:

Bash
./mvnw spring-boot:run
Set up Automation:
Install n8n locally via npx n8n and import the provided workflow JSON from the /automation folder.

🚀 Future Roadmap
[ ] Extended Support: Add .docx and .xlsx parsing.

[ ] Vision AI: Direct analysis of JPG/PNG screenshots.

[ ] Frontend: A React-based dashboard for visual document management.

[ ] Auth: Implementation of OAuth2/JWT for multi-tenant security.

Author: Ranindu Amarasinghe
>>>>>>> f336789 (fix: prevent duplicate documents from being stored in database)
