package com.rapidminer.fileupload.controller;

import com.rapidminer.fileupload.model.UploadResponse;
import com.rapidminer.fileupload.service.upload.UploadException;
import com.rapidminer.fileupload.service.upload.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(path = "api")
public class UploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();

        try {
            UUID id = this.uploadService.startUpload(fileName, file.getInputStream());

            return ResponseEntity
                    .accepted()
                    .body(new UploadResponse(id));
        } catch (UploadException | IOException e) {
            LOGGER.error("Error while storing the file with name: '{}'. Reason: ", fileName, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
