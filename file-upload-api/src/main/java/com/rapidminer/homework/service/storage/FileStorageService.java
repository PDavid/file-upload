package com.rapidminer.homework.service.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

    // TODO: Move this to config!
    private final Path fileStorageLocation = Path.of("/home/paksyd-private/projects/homework/media/upload");

    public UUID storeFile(UUID id, String fileName, InputStream inputStream) {
        LOGGER.debug("Storing file with id: '{}', file name: '{}'...", id, fileName);

        Path directory = this.fileStorageLocation.resolve(id.toString());

        try {
            Files.createDirectories(directory);

            Path targetLocation = directory.resolve(fileName);

            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            LOGGER.debug("Successfully stored file with id: '{}', file name: '{}' to location: '{}'", id, fileName, targetLocation);

            return id;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ".", e);
        }
    }
}
