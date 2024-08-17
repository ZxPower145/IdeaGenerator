package org.nexus.ideagenerator.core.controllers;

import org.nexus.ideagenerator.core.components.PDFRequest;
import org.nexus.ideagenerator.core.services.LLMService;
import org.nexus.ideagenerator.core.services.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://127.0.0.1:8080")
public class IdeaGeneratorController {
    private final LLMService llmService;

    @Autowired
    public IdeaGeneratorController(LLMService llmService) {
        this.llmService = llmService;
    }

    @GetMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generate() {
        String response = llmService.generateResponse();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate_pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public void generatePdf(@RequestBody PDFRequest pdfRequest) {
        PDFGenerator.create(
                pdfRequest.getTitle(),
                pdfRequest.getSlogan(),
                pdfRequest.getPitch(),
                pdfRequest.getDescription(),
                pdfRequest.getDifficulty(),
                pdfRequest.getSuccess(),
                pdfRequest.getApiToUse(),
                pdfRequest.getTags()
        );
    }

    @PostMapping("/generate_md")
    public void generateMd() {

    }
}

