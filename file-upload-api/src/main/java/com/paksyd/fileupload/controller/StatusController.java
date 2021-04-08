package com.paksyd.fileupload.controller;

import com.paksyd.fileupload.service.upload.UploadService;
import com.paksyd.fileupload.model.Status;
import com.paksyd.fileupload.model.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "{id}")
    public ResponseEntity<StatusResponse> getStatus(@PathVariable("id") UUID id) {
        // TODO: Check if id can be null here and return 400 if it is!
        Status status = this.uploadService.getStatus(id);
        return ResponseEntity.ok(new StatusResponse(status));
    }
}
