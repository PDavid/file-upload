package com.paksyd.fileupload.service.storage;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paksyd.fileupload.model.FileContentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    private final Path fileStorageLocation = Path.of("upload");

    public void storeFile(UUID id, String fileName, InputStream inputStream) {
        LOGGER.debug("Storing file with id: '{}', file name: '{}'...", id, fileName);

        Path directory = this.fileStorageLocation.resolve(id.toString());

        try {
            Files.createDirectories(directory);

            Path targetLocation = directory.resolve(fileName);

            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            LOGGER.debug("Successfully stored file with id: '{}', file name: '{}' to location: '{}'", id, fileName, targetLocation);
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ".", e);
        }
    }

    public FileContentResponse readFile(UUID id) {
        Path directory = this.fileStorageLocation.resolve(id.toString());

        String[] files = directory.toFile().list(new ExcelFilenameFilter());
        if (files == null || files.length == 0) {
            throw new FileStorageException("Could not read file with id: " + id + ".");
        }
        String fileName = files[0];

        LOGGER.debug("file name: '{}'", fileName);

        String jsonFileName = fileName
                .replaceAll(".xlsx", ".json");

        Path jsonFile = directory.resolve(jsonFileName);

        LOGGER.debug("jsonFile: '{}'", jsonFile);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

            return mapper.readValue(jsonFile.toFile(), FileContentResponse.class);
        } catch (Exception e) {
            throw new FileStorageException("Could not read file " + jsonFile + ".", e);
        }
    }
}
