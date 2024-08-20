package org.nexus.ideagenerator.core.controllers;

import org.nexus.ideagenerator.core.components.MDRequest;
import org.nexus.ideagenerator.core.components.PDFRequest;
import org.nexus.ideagenerator.core.models.Api;
import org.nexus.ideagenerator.core.repository.ApiRepository;
import org.nexus.ideagenerator.core.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://127.0.0.1:8080")
public class IdeaGeneratorController {
    private final ApiRepository apiRepository;
    private final LLMService llmService;

    @Autowired
    public IdeaGeneratorController(LLMService llmService, ApiRepository apiRepository) {
        this.llmService = llmService;
        this.apiRepository = apiRepository;
    }

    @GetMapping(value = "/get/api/all")
    public ResponseEntity<List<Api>> getAllApis(){
        return ResponseEntity.ok(apiRepository.findAll());
    }

    @GetMapping(value = "/get/api/category/{category}")
    public ResponseEntity<List<Api>> getAllApisByCategory(@PathVariable String category) {
        return ResponseEntity.ok(apiRepository.findAllByCategory(category));
    }

    @GetMapping(value = "/get/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> allCategories = new ArrayList<>();
        for (Api api : apiRepository.findAll()) {
            if (!allCategories.contains(api.getCategory())) {
                allCategories.add(api.getCategory());
            };
        }
        return ResponseEntity.ok(allCategories);
    }

    @GetMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generate() {
        String response = llmService.generateResponse();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate_pdf")
    public HttpStatus generatePdf(@RequestBody PDFRequest pdfRequest) {
        return PDFManager.create(pdfRequest);
    }

    @GetMapping( "/download_pdf/{title}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String title) {
        return FileManager.download(title, "pdf");
    }

    @PostMapping("delete_pdf/{title}")
    public HttpStatus deletePdf(@PathVariable String title) {
        return FileManager.delete(title, "pdf");
    }

    @PostMapping("/generate_md")
    public HttpStatus generateMd(@RequestBody MDRequest mdRequest) {
        return MDManager.create(mdRequest);
    }

    @GetMapping("/download_md/{title}")
    public ResponseEntity<Resource> downloadMd(@PathVariable String title) {
        return FileManager.download(title, "md");
    }

    @PostMapping("/delete_md/{title}")
    public HttpStatus deleteMd(@PathVariable String title) {
        return FileManager.delete(title, "md");
    }
}

