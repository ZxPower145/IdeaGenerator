package org.nexus.ideagenerator.core.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("LLM")
public class LLM {
    @Value("${llm.model}")
    private String model;
    @Value("${llm.api}")
    private String api;
    @Value("${llm.apiKey}")
    private String apiKey;

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public String getApi() { return api; }

    public void setApi(String api) { this.api = api; }

    public String getApiKey() { return apiKey; }

    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

}