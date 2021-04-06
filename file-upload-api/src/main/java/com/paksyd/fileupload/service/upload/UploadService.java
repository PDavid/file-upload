package com.paksyd.fileupload.service.upload;

import com.paksyd.fileupload.service.converter.ConverterService;
import com.paksyd.fileupload.model.Status;
import com.paksyd.fileupload.service.storage.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UploadService {

    private static final Logger LOG = LoggerFactory.getLogger(UploadService.class);

    private final Map<UUID, Status> statuses = new ConcurrentHashMap<>();

    private final ConverterService converterService;
    private final FileStorageService fileStorageService;

    @Autowired
    public UploadService(ConverterService converterService, FileStorageService fileStorageService) {
        this.converterService = converterService;
        this.fileStorageService = fileStorageService;
    }

    public UUID startUpload(String fileName, InputStream inputStream) {
        validateFileName(fileName);

        UUID id = UUID.randomUUID();

        LOG.debug("Started to store new file with name: '{}' and id: '{}'...", fileName, id);

        storeFile(id, fileName, inputStream)
                .handle((success, throwable) -> {
                    if (throwable != null) {
                        LOG.error("Failed to store file with id: '{}', name: '{}'.", id, fileName, throwable);
                        LOG.debug("*** New status: '{}'", Status.UPLOAD_ERROR);
                        statuses.put(id, Status.UPLOAD_ERROR);
                    } else {
                        startConverting(id, fileName);
                    }
                    return success;
                });

        return id;
    }

    @Async
    public CompletableFuture<Boolean> storeFile(UUID id, String fileName, InputStream inputStream) {
        LOG.debug("Starting storing of file with id: '{}', name: '{}'...", id, fileName);

        LOG.debug("*** New status: '{}'", Status.UPLOADING);
        statuses.put(id, Status.UPLOADING);

        fileStorageService.storeFile(id, fileName, inputStream);

        LOG.debug("Successfully stored file with id: '{}', name: '{}'...", id, fileName);

        return CompletableFuture.completedFuture(true);
    }

    private void startConverting(UUID id, String fileName) {
        LOG.debug("Starting converting file with id: '{}', name: '{}'...", id, fileName);

        LOG.debug("*** New status: '{}'", Status.CONVERTING);
        statuses.put(id, Status.CONVERTING);

        converterService.convert(id, fileName)
                .handle((success, throwable) -> {
                    if (throwable != null) {
                        LOG.error("Failed to convert file with id: '{}', name: '{}'.", id, fileName, throwable);
                        LOG.debug("*** New status: '{}'", Status.CONVERSION_ERROR);
                        statuses.put(id, Status.CONVERSION_ERROR);
                    } else {
                        LOG.debug("Successfully finished converting file with id: '{}', name: '{}'.", id, fileName);
                        LOG.debug("*** New status: '{}'", Status.CONVERTED);
                        statuses.put(id, Status.CONVERTED);
                    }
                    return success;
                });
    }

    public Status getStatus(UUID id) {
        return statuses.getOrDefault(id, Status.UNKNOWN);
    }

    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new UploadException("File name cannot be null or empty.");
        }

        String cleanedFileName = StringUtils.cleanPath(fileName);

        if(cleanedFileName.contains("..")) {
            throw new UploadException("File name cannot contain invalid path sequence. Got: " + fileName);
        }

        if (!isExcelFile(cleanedFileName)) {
            throw new UploadException("Only Excel (XLS, XLSX) files can be uploaded.");
        }
    }

    boolean isExcelFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return false;
        }

        String fileExtension = "";
        try {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch(Exception e) {
            LOG.debug("Failed to extract file extension from file name: '{}'", fileName, e);
        }
        return "xlsx".equals(fileExtension) ||
                "xls".equalsIgnoreCase(fileExtension);
    }
}
