package com.paksyd.fileupload.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Row {

    @JsonProperty
    private final List<String> columns;

    @JsonCreator
    public Row(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        return "Row{" +
                "columns=" + columns +
                '}';
    }
}
