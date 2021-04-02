package com.rapidminer.homework.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusResponse {

    @JsonProperty
    private final Status status;

    public StatusResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
