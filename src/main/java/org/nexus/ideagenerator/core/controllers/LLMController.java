package org.nexus.ideagenerator.core.controllers;

import org.nexus.ideagenerator.core.components.enums.RequestType;
import org.nexus.ideagenerator.core.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling LLM-related HTTP requests.
 * This controller provides endpoints for getting responses from the LLM and generating project ideas.
 */
@RestController
@RequestMapping("/api/llm")
@CrossOrigin("http://127.0.0.1:8080") // Allows cross-origin requests from the specified URL
public class LLMController {
    private final LLMService llmService;

    /**
     * Constructor for LLMController.
     * @param llmService The LLMService to be used for processing requests.
     */
    @Autowired
    public LLMController(LLMService llmService) {
        this.llmService = llmService;
    }

    /**
     * Endpoint for getting a response from the LLM based on user input.
     * @param input The user's input message.
     * @return ResponseEntity containing the LLM's response.
     */
    @PostMapping(value = "/get/response")
    public ResponseEntity<String> getResponse(@RequestBody String input) {
        String response = llmService.processRequest(input, RequestType.USER_MESSAGE);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for getting project ideas based on a specified category.
     * @param category The category for which to generate project ideas.
     * @return ResponseEntity containing the generated project ideas in JSON format.
     */
    @GetMapping(value = "/get/ideas/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getIdeasByCategory(@PathVariable String category) {
        String response = llmService.processRequest(category, RequestType.PROJECT_IDEA);
        return ResponseEntity.ok(response);
    }
}