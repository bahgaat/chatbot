package com.example.chatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
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


@SpringBootApplication
public class ChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatbotApplication.class, args);
	}


	@Bean
	public CommandLineRunner cli(@Value("classpath:Resume.pdf") Resource docs,
								 ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		return args -> {

			vectorStore.add(new TokenTextSplitter().split(new PagePdfDocumentReader(docs).read()));


			ChatMemory chatMemory = MessageWindowChatMemory.builder().chatMemoryRepository(new InMemoryChatMemoryRepository())
					.build();


			MessageChatMemoryAdvisor memoryAdvisor =
					MessageChatMemoryAdvisor
							.builder(chatMemory)
							.build();
			var chatClient = chatClientBuilder
					.defaultSystem("You are useful assistant.")// Set the system prompt
					.defaultAdvisors(memoryAdvisor)// Enable chat memory
					.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))// Enable RAG
					.build();

			System.out.println("\nI am your  assistant.\n");
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					System.out.print("\nUSER: ");
					System.out.println("\nASSISTANT: " +
							chatClient.prompt(scanner.nextLine())
									.call()
									.content());

				}
			}
		};
	}
}


