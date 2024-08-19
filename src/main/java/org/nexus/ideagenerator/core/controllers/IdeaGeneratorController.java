package org.nexus.ideagenerator.core.controllers;

import org.nexus.ideagenerator.core.components.MDRequest;
import org.nexus.ideagenerator.core.components.PDFRequest;
import org.nexus.ideagenerator.core.services.FileManager;
import org.nexus.ideagenerator.core.services.LLMService;
import org.nexus.ideagenerator.core.services.MDManager;
import org.nexus.ideagenerator.core.services.PDFManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

