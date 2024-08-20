package org.nexus.ideagenerator.core.services;

import jakarta.annotation.PostConstruct;
import org.nexus.ideagenerator.core.models.Api;
import org.nexus.ideagenerator.core.repository.ApiRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PromptGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PromptGenerator.class);

    @Value("${llm.initialPrompt}")
    private String initialPrompt;

    @Value("${llm.noOfPicks}")
    private int noOfPicks;

    private List<Api> apiList;

    private final ApiRepository apiRepository;

    public PromptGenerator(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
        this.apiList = populateApiList();
    }

    private List<Api> populateApiList() {
        return apiRepository.findAll();
    }

    private List<String> pickRandomApis() {
        Random rand = new Random();
        List<String> pickedApis = new ArrayList<>(noOfPicks);

        if (apiList.isEmpty()) {
            return pickedApis;
        }

        while (pickedApis.size() < noOfPicks) {
            int random = rand.nextInt(apiList.size());
            pickedApis.add(apiList.remove(random).toString());
        }

        return pickedApis;
    }

    public String getGeneratedPrompt() {
        return initialPrompt + pickRandomApis().toString();
    }
}