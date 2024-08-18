package org.nexus.ideagenerator.core.services;

import org.springframework.http.HttpStatus;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MDManager {
    private static final String DEST = "src/main/resources/md/";

    public static HttpStatus create(
            String title,
            String slogan,
            String pitch,
            String description,
            String success,
            String difficulty,
            String apiToUse,
            ArrayList<String> tags
    ) {
        try {
            FileWriter file = new FileWriter(DEST + title + ".md");
            BufferedWriter writer = new BufferedWriter(file);

            // Title
            writer.write("# **" + title + "**");
            writer.newLine();
            writer.newLine();

            writer.write("### **" + slogan + "**");
            writer.newLine();
            writer.write("---");
            writer.newLine();
            writer.newLine();

            writer.write("## Description");
            writer.newLine();
            writer.write(description);
            writer.newLine();
            writer.newLine();

            writer.write("## Pitch");
            writer.newLine();
            writer.write(pitch);
            writer.newLine();
            writer.newLine();

            writer.write("## Details");
            writer.newLine();
            writer.write("- **Difficulty:** " + difficulty);
            writer.newLine();
            writer.write("- **Success Rate:** " + success);
            writer.newLine();
            writer.write("- **API To Use:** " + apiToUse);
            writer.newLine();
            writer.newLine();

            writer.write("## Tags");
            writer.newLine();
            for (String tag : tags) {
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
