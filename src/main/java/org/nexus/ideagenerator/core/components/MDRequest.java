package org.nexus.ideagenerator.core.components;
import java.util.ArrayList;

public class MDRequest {
    private String title;
    private String slogan;
    private String pitch;
    private String description;
    private String difficulty;
    private String success;
    private String apiToUse;
    private ArrayList<String> tags;

    public MDRequest(String title, String slogan, String pitch, String description, String difficulty, String success, String apiToUse, ArrayList<String> tags) {
        this.title = title;
        this.slogan = slogan;
        this.pitch = pitch;
        this.description = description;
        this.difficulty = difficulty;
        this.success = success;
        this.apiToUse = apiToUse;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getApiToUse() {
        return apiToUse;
    }

    public void setApiToUse(String apiToUse) {
        this.apiToUse = apiToUse;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
