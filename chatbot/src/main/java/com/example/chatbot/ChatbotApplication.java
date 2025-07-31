package com.example.chatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.Scanner;
import java.util.UUID;


// @formatter:off
@SpringBootApplication
public class ChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatbotApplication.class, args);
	}


//@Controller
//@ResponseBody
//class ChatController {
//	private final ChatClient ai;
//
//	ChatController(ChatClient.Builder ai) {
//		this.ai = ai.build();
//
//	}
//	@GetMapping("/")
//	public String index(@RequestParam String message){
//		return this.ai.prompt()
//				.system("You are a helpful assistant")
//				.user(message)
//				.call()
//				.content();
//	}
//}



	@Bean
	public CommandLineRunner cli(@Value("classpath:Bahgat_cv.pdf") Resource docs,
								 ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		return args -> {

			// 1. Load the hurricane documents in vector store
			vectorStore.add(new TokenTextSplitter().split(new PagePdfDocumentReader(docs).read()));


			// 2. Create the ChatClient with chat memory and RAG support
			ChatMemory chatMemory = MessageWindowChatMemory.builder().chatMemoryRepository(new InMemoryChatMemoryRepository())
					.maxMessages(10)
					.build();

		//	var advisor = PromptChatMemoryAdvisor.builder(chatMemory).build();
			String conversationId = UUID.randomUUID().toString();
			MessageChatMemoryAdvisor advisor =
					MessageChatMemoryAdvisor
							.builder(chatMemory)
							.conversationId(conversationId)
							.build();
			var chatClient = chatClientBuilder
					//.defaultSystem("You are a helpful coding assistant")
					.defaultSystem("You are a self assistant.")// Set the system prompt
					.defaultAdvisors(advisor)// Enable chat memory
					.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))// Enable RAG
					.build();

			// 3. Start the chat loop
			System.out.println("\nI am your cv assistant.\n");
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					System.out.print("\nUSER: ");
					System.out.println("\nASSISTANT: " +
							chatClient.prompt(scanner.nextLine())
								//	.advisors(advisor)// Get the user input
									.call()
									.content());

				}
			}
		};
	}
}


