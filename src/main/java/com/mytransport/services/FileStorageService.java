package com.mytransport.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path baseDir;

    public FileStorageService(@Value("${app.upload.base-dir:uploads}") String baseDir) {
        this.baseDir = Paths.get(baseDir).toAbsolutePath().normalize();
    }

    public String storeTitleScan(Long vehicleId, MultipartFile file) throws IOException {
        return storeFile("titles", "vehicle-" + vehicleId, file);
    }

    public String storePhoto(Long vehicleId, MultipartFile file) throws IOException {
        return storeFile("photos", "vehicle-" + vehicleId, file);
    }

    public String storeDocument(Long vehicleId, MultipartFile file) throws IOException {
        return storeFile("documents", "vehicle-" + vehicleId, file);
    }

    public Resource loadAsResource(String relativePath) {
        Path filePath = baseDir.resolve(relativePath).normalize();
        return new FileSystemResource(filePath);
    }

    public Path resolvePath(String relativePath) {
        return baseDir.resolve(relativePath).normalize();
    }

    public boolean deleteIfExists(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return false;
        }
        try {
            return Files.deleteIfExists(baseDir.resolve(relativePath).normalize());
        } catch (IOException e) {
            return false;
        }
    }

    private String storeFile(String category, String prefix, MultipartFile file) throws IOException {
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot > -1) {
            ext = originalName.substring(dot);
        }

        Path targetDir = baseDir.resolve(category).resolve(prefix);
        Files.createDirectories(targetDir);

        String filename = UUID.randomUUID() + ext;
        Path targetFile = targetDir.resolve(filename);
        Files.copy(file.getInputStream(), targetFile);

        return baseDir.relativize(targetFile).toString().replace('\\', '/');
    }
}
