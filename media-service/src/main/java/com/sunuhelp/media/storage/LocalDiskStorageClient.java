package com.sunuhelp.media.storage;

import com.sunuhelp.media.exception.FileNotFoundBusinessException;
import com.sunuhelp.media.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class LocalDiskStorageClient implements MediaStorageClient {

    private static final Logger log = LoggerFactory.getLogger(LocalDiskStorageClient.class);

    private final Path storageRoot;

    public LocalDiskStorageClient(@Value("${app.storage.local-path}") String localPath) {
        this.storageRoot = Paths.get(localPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(storageRoot);
        } catch (IOException e) {
            throw new StorageException();
        }
    }

    @Override
    public String store(MultipartFile file, UUID mediaFileId) {
        try {
            String extension = extractExtension(file.getOriginalFilename());
            String fileName = mediaFileId + extension;
            Path targetPath = storageRoot.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            log.error("Echec store() pour {} : {} - {}", mediaFileId, e.getClass().getName(), e.getMessage());
            throw new StorageException();
        }
    }

    @Override
    public byte[] read(String fileUrl) {
        try {
            return Files.readAllBytes(storageRoot.resolve(fileUrl));
        } catch (NoSuchFileException e) {
            throw new FileNotFoundBusinessException();
        } catch (IOException e) {
            log.error("Echec read() pour {} (racine={}) : {} - {}",
                    fileUrl, storageRoot, e.getClass().getName(), e.getMessage());
            throw new StorageException();
        }
    }

    @Override
    public void delete(String fileUrl) {
        try {
            Files.deleteIfExists(storageRoot.resolve(fileUrl));
        } catch (IOException e) {
            throw new StorageException();
        }
    }

    private String extractExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }
}
