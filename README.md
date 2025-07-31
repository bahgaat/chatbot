# Spring AI Chat Bot CLI

AI-powered chatbot with domain-specific knowledge (in this case, about Resume) using Spring AI, Retrieval-Augmented Generation RAG and Conversational Memory.

## Key Features
- RAG Implementation: The application uses a vector store to implement RAG, allowing the chatbot to retrieve relevant information from the loaded document.
- Conversation Memory: The MessageChatMemoryAdvisor enables the chatbot to remember previous interactions within the conversation.
- PDF Document Processing: The application can read and process PDF documents, making the information available to the chatbot.
- Interactive Console Interface: The application provides a simple console-based interface for interacting with the chatbot.

# Auto-configurations
## AI Model
By default, this project uses Ollama/phi3:mini Imp: you have to pull the model. However, you can easily switch to any other supported AI model. The pom.xml file prvidew few alternative AI model dependencies. (Note: Most models, except Ollama, require an API key for access.) Configure your API key and other model properties in the application.properties file. The Chat Model API lists all supported modesl.

## Vector Store
The project is configured to use Chroma (spring-ai-chroma-store-spring-boot-starter) as a vector store, running locally: A docker-compose.yaml file is provided to start a local Chroma instance. The project is configured with Spring Boot Docker Compose integration for easy setup. (e.g. you don't have to start the docker-compose manually). Find more about Vector Stores

## PDF Document Processing
PDF document reading capability is enabled through the spring-ai-pdf-document-reader dependency. Find more about the Spring AI document indexing support
