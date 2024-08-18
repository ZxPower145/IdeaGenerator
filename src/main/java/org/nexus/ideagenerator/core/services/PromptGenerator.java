package org.nexus.ideagenerator.core.services;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PromptGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PromptGenerator.class);

    @Value("${llm.initialPrompt}")
    private String initialPrompt;

    @Value("${llm.noOfPicks:5}")
    private int noOfPicks;

    @Value("${llm.apiListPath}")
    private String apiListPath;

    private String generatedPrompt = "";
    private List<String> apiList = new ArrayList<>();

    @PostConstruct
    public void init() {
        if (this.apiList == null || this.apiList.isEmpty()) {
            this.apiList = populateApiList();
        } else {
            this.apiList = new ArrayList<>();
        }
        if (generatedPrompt.isEmpty() || generatedPrompt.isBlank()) {
            this.generatedPrompt = initialPrompt + "[" + String.join(", ", apiList) + "]";
        } else {
            this.generatedPrompt = "";
        }
    }

    private List<String> populateApiList() {
        try {
            List<String> allApis = readApiList();
            return pickRandomApis(allApis);
        } catch (IOException e) {
            logger.error("Error reading API list file", e);
            return new ArrayList<>();
        }
    }

    private List<String> readApiList() throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource(apiListPath).getInputStream()
                )
        )) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    private List<String> pickRandomApis(List<String> allApis) {
        Random rand = new Random();
        List<String> pickedApis = new ArrayList<>(noOfPicks);

        while (pickedApis.size() < noOfPicks) {
            int random = rand.nextInt(allApis.size());
            pickedApis.add(allApis.remove(random));
        }

        return pickedApis;
    }

    public String getGeneratedPrompt() {
        return generatedPrompt;
    }
}