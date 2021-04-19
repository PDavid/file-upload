package com.paksyd.fileupload.controller;

import com.paksyd.fileupload.model.FileContentResponse;
import com.paksyd.fileupload.service.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/file")

public class ContentController {

    private final FileStorageService fileStorageService;

    @Autowired
    public ContentController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "{id}")
    public ResponseEntity<FileContentResponse> getStatus(@PathVariable("id") UUID id) {
        // TODO: Check if id can be null here and return 400 if it is!
        FileContentResponse fileContent = this.fileStorageService.readFile(id);
        return ResponseEntity.ok(fileContent);
    }
}
