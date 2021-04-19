package com.paksyd.fileupload.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileContentResponse {

    @JsonProperty
    private final List<Row> rows;

    @JsonCreator
    public FileContentResponse(List<Row> rows) {
        this.rows = rows;
    }

    public List<Row> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "FileContentResponse{" +
                "rows=" + rows +
                '}';
    }
}
