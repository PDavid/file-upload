package com.paksyd.fileupload.service.storage;

import java.io.File;
import java.io.FilenameFilter;

public class ExcelFilenameFilter implements FilenameFilter {
    @Override
    public boolean accept(File f, String name) {
        return name.endsWith(".xlsx");
    }
}
