package org.nexus.ideagenerator.core.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.nexus.ideagenerator.core.components.LLM;
import org.nexus.ideagenerator.core.components.Message;
import org.nexus.ideagenerator.core.components.enums.RequestType;
import org.nexus.ideagenerator.core.components.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling interactions with a Language Learning Model (LLM).
 * This service processes user requests, generates project ideas, and communicates with the LLM API.
 */
@Service("LLMService")
public class LLMService {
    private final LLM llm;
    private final PromptGenerator promptGenerator;
    private final HttpClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(LLMService.class);

    private List<Message> messageList = new ArrayList<>();

    /**
     * Constructor for LLMService.
     * @param llm The LLM component.
     * @param promptGenerator The PromptGenerator component.
     */
    @Autowired
    public LLMService(LLM llm, PromptGenerator promptGenerator) {
        this.llm = llm;
        this.promptGenerator = promptGenerator;
        this.client = HttpClient.newHttpClient();
        initializeMessageList();
    }

    /**
     * Initializes the message list with a system message.
     */
    private void initializeMessageList() {
        messageList.add(new Message(Role.system, "You are a helpful assistant."));
    }

    /**
     * Processes the user's request based on the request type.
     * @param input The user's input.
     * @param requestType The type of request (USER_MESSAGE or PROJECT_IDEA).
     * @return The generated response.
     */
    public String processRequest(String input, RequestType requestType) {
        return switch (requestType) {
            case USER_MESSAGE -> handleUserMessage(input);
            case PROJECT_IDEA -> generateProjectIdeas(input);
            default -> throw new IllegalArgumentException("Unsupported request type");
        };
    }

    /**
     * Handles a user message by adding it to the message list and generating a response.
     * @param userMessage The user's message.
     * @return The generated response.
     */
    private String handleUserMessage(String userMessage) {
        messageList.add(new Message(Role.user, userMessage));
        String response = generateResponse();
        messageList.add(new Message(Role.assistant, response));
        return response;
    }

    /**
     * Generates project ideas based on a given category.
     * @param category The category for project ideas.
     * @return The generated project ideas.
     */
    private String generateProjectIdeas(String category) {
        String prompt = promptGenerator.getGeneratedPrompt(category);
        messageList.clear();
        initializeMessageList();
        messageList.add(new Message(Role.system, "You are a project idea generator."));
        messageList.add(new Message(Role.user, prompt));
        String response = generateResponse();
        messageList.add(new Message(Role.assistant, response));
        return response;
    }

    /**
     * Generates a response from the LLM based on the current message list.
     * @return The generated response.
     */
    private String generateResponse() {
        String requestBody = createRequestBody();
        return sendRequest(requestBody);
    }

    /**
     * Creates the JSON request body for the LLM API.
     * @return The JSON request body as a string.
     */
    private String createRequestBody() {
        try {
            ObjectNode rootNode = objectMapper.createObjectNode();
            ArrayNode messagesNode = rootNode.putArray("messages");

            for (Message message : messageList) {
                ObjectNode messageNode = messagesNode.addObject();
                messageNode.put("role", message.getRole().toString().toLowerCase());
                messageNode.put("content", message.getContent());
            }

            rootNode.put("model", llm.getModel());

            return objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            logger.error("Error creating request body", e);
            return "{}";
        }
    }

    /**
     * Sends a request to the LLM API and returns the response.
     * @param requestBody The JSON request body.
     * @return The parsed response from the LLM API.
     */
    private String sendRequest(String requestBody) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(llm.getApi()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + llm.getApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return extractContentFromResponse(response.body());
        } catch (IOException | InterruptedException e) {
            logger.error("Error sending request to LLM API", e);
            return "An error occurred";
        }
    }

    /**
     * Extracts the content from the LLM API response.
     * @param responseBody The response body from the LLM API.
     * @return The extracted content or an error message.
     */
    private String extractContentFromResponse(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            if (rootNode.has("error")) {
                JsonNode errorNode = rootNode.get("error");
                String errorMessage = errorNode.get("message").asText();
                logger.error("API Error: {}", errorMessage);
                return "An error occurred: " + errorMessage;
            }
            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                JsonNode firstChoice = choicesNode.get(0);
                JsonNode messageNode = firstChoice.path("message");
                JsonNode contentNode = messageNode.path("content");
                if (contentNode.isTextual()) {
                    return contentNode.asText();
                }
            }
            logger.warn("Unexpected response format: {}", responseBody);
        } catch (Exception e) {
            logger.error("Error parsing JSON response", e);
        }
        return "Unable to extract content from response";
    }
}