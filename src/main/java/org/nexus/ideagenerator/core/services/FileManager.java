package org.nexus.ideagenerator.core.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private static final String DEST = "src/main/resources/";

    public static ResponseEntity<Resource> download(String title, String extension) {
        try {
            File file = new File(DEST + extension + "/" + title + "." +extension);
            if (file.exists()) {
                Path path = Paths.get(file.getAbsolutePath());
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                        .header(HttpHeaders.CONTENT_TYPE, "application/" + extension)
                        .body(resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    public static HttpStatus delete(String title, String extension) {
        File fileToDelete = new File(DEST + extension + "/" + title + "." +extension);
        boolean deleted = fileToDelete.delete();

        if (deleted) {
            return HttpStatus.OK;
        }

        return HttpStatus.NOT_FOUND;
    }
}
