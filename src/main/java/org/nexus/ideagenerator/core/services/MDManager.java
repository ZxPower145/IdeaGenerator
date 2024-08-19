package org.nexus.ideagenerator.core.services;

import org.nexus.ideagenerator.core.components.MDRequest;
import org.springframework.http.HttpStatus;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MDManager {
    private static final String DEST = "src/main/resources/md/";

    public static HttpStatus create(MDRequest mdRequest) {
        try {
            FileWriter file = new FileWriter(DEST + mdRequest.getTitle() + ".md");
            BufferedWriter writer = new BufferedWriter(file);

            // Title
            writer.write("# **" + mdRequest.getTitle() + "**");
            writer.newLine();
            writer.newLine();

            writer.write("### **" + mdRequest.getSlogan() + "**");
            writer.newLine();
            writer.write("---");
            writer.newLine();
            writer.newLine();

            writer.write("## Description");
            writer.newLine();
            writer.write(mdRequest.getDescription());
            writer.newLine();
            writer.newLine();

            writer.write("## Pitch");
            writer.newLine();
            writer.write(mdRequest.getPitch());
            writer.newLine();
            writer.newLine();

            writer.write("## Details");
            writer.newLine();
            writer.write("- **Difficulty:** " + mdRequest.getDifficulty());
            writer.newLine();
            writer.write("- **Success Rate:** " + mdRequest.getSuccess());
            writer.newLine();
            writer.write("- **API To Use:** " + mdRequest.getApiToUse());
            writer.newLine();
            writer.newLine();

            writer.write("## Tags");
            writer.newLine();
            for (String tag : mdRequest.getTags()) {
                writer.write("- " + tag);
                writer.newLine();
            }

            writer.close();

            return HttpStatus.CREATED;
        } catch (IOException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
