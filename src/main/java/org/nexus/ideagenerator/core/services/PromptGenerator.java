package org.nexus.ideagenerator.core.services;

import org.nexus.ideagenerator.core.models.Api;
import org.nexus.ideagenerator.core.repository.ApiRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service class for generating prompts based on APIs.
 * This class is responsible for creating prompts that include information about selected APIs.
 */
@Service
public class PromptGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PromptGenerator.class);

    // Initial prompt text loaded from application properties
    @Value("${llm.initialPrompt}")
    private String initialPrompt;

    // Number of APIs to pick, loaded from application properties
    @Value("${llm.noOfPicks}")
    int noOfPicks;

    private final ApiRepository apiRepository;

    /**
     * Constructor for PromptGenerator.
     * @param apiRepository The repository for accessing API data.
     */
    public PromptGenerator(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    /**
     * Populates a list of APIs based on the given category.
     * If the category is null, empty, or "random", it picks random APIs.
     * @param category The category of APIs to retrieve.
     * @return A list of APIs.
     */
    private List<Api> populateApiList(String category) {
        if (category == null || category.isEmpty() || category.equals("random")) {
            logger.warn("Random");
            return pickRandomApis(apiRepository.findAll());
        }
        return apiRepository.findAllByCategory(category.replaceAll("%20", " "));
    }

    /**
     * Picks a random subset of APIs from the given list.
     * @param apiList The list of APIs to choose from.
     * @return A list of randomly selected APIs.
     */
    private List<Api> pickRandomApis(List<Api> apiList) {
        Random random = new Random();
        List<Api> randomApis = new ArrayList<>();

        if (apiList.isEmpty()) { return apiList; }

        while (randomApis.size() < noOfPicks && !apiList.isEmpty()) {
            int randomIndex = random.nextInt(apiList.size());
            randomApis.add(apiList.remove(randomIndex));
        }

        return randomApis;
    }

    /**
     * Generates a prompt by combining the initial prompt with information about selected APIs.
     * @param category The category of APIs to include in the prompt.
     * @return The generated prompt as a string.
     */
    public String getGeneratedPrompt(String category) {
        return initialPrompt + populateApiList(category);
    }
}