package com.rapidminer.homework.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class UploadResponse {

    @JsonProperty
    private final UUID id;

    public UploadResponse(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
