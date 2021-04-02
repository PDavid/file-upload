package com.rapidminer.homework.controller;

import com.rapidminer.homework.model.Status;
import com.rapidminer.homework.model.StatusResponse;
import com.rapidminer.homework.service.upload.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/status")
public class StatusController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);

    private final UploadService uploadService;

    @Autowired
    public StatusController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(path = "{id}")
    public ResponseEntity<StatusResponse> getStatus(@PathVariable("id") UUID id) {
        // TODO: Check if id can be null here and return 400 if it is!
        Status status = this.uploadService.getStatus(id);
        return ResponseEntity.ok(new StatusResponse(status));
    }
}
