package com.example.SQLMLProject.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Spring AI with OpenAI integration.
 * Configures the ChatClient bean for LLM interactions.
 */
@Configuration
public class OpenAIConfig {

    /**
     * Creates a ChatClient bean for communicating with OpenAI.
     * The API key is automatically picked up from OPENAI_API_KEY environment variable.
     *
     * @return ChatClient bean
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
