package org.nexus.ideagenerator.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.nexus.ideagenerator.core.components.LLM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service("LLMService")
public class LLMService {
    private LLM llm;
    private PromptGenerator promptGenerator;
    private final HttpClient client;
    @Value("${llm.maxTokens}")
    private int maxTokens;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(LLMService.class);

    @Autowired
    public LLMService(LLM llm, PromptGenerator promptGenerator) {
        this.llm = llm;
        this.promptGenerator = promptGenerator;
        this.client = HttpClient.newHttpClient();
    }

    public LLM getLlm() {
        return llm;
    }

    public void setLlm(LLM llm) {
        this.llm = llm;
    }

    public PromptGenerator getPromptGenerator() {
        return promptGenerator;
    }

    public void setPromptGenerator(PromptGenerator promptGenerator) {
        this.promptGenerator = promptGenerator;
    }

    private String escapeJSON(String input){
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public String generateResponse() {
        String requestBody = String.format(
            """
                {
                    "messages": [
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ],
                    "model": "%s"
                }
            """, escapeJSON(promptGenerator.getGeneratedPrompt()), llm.getModel()
        );

        System.out.println(llm.getApiKey());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(llm.getApi()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + llm.getApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(extractContentFromResponse(response.body()));
            return extractContentFromResponse(response.body());
        } catch (IOException e) {
            logger.error("org.nexus.ideagenerator.services.LLMService IOException");
        } catch (InterruptedException e) {
            logger.error("org.nexus.ideagenerator.services.LLMService InterruptedException");
        }
        return "An error occurred";
    }

    private String extractContentFromResponse(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                JsonNode firstChoice = choicesNode.get(0);
                JsonNode messageNode = firstChoice.path("message");
                JsonNode contentNode = messageNode.path("content");
                if (contentNode.isTextual()) {
                    return contentNode.asText();
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing JSON response", e);
        }
        return "Unable to extract content from response";
    }
}
