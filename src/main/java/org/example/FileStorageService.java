package org.example;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
        }
    }
}