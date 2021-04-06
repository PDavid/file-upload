package com.paksyd.fileupload.service.upload;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UploadServiceTest {

    UploadService uploadService = new UploadService(null, null);

    @Test
    void isExcelFile_shouldReturnFalse_whenNotExcel() {
        assertFalse(uploadService.isExcelFile("file1.txt"));
        assertFalse(uploadService.isExcelFile("file22.doc"));
        assertFalse(uploadService.isExcelFile("entity.xml"));
        assertFalse(uploadService.isExcelFile("package.json"));
    }

    @Test
    void isExcelFile_shouldReturnFalse_whenNoExtension() {
        assertFalse(uploadService.isExcelFile("file1"));
        assertFalse(uploadService.isExcelFile("doc"));
    }

    @Test
    void isExcelFile_shouldReturnTrue_whenExcel() {
        assertTrue(uploadService.isExcelFile("spreadsheet1.xls"));
        assertTrue(uploadService.isExcelFile("spreadsheet12.xlsx"));
    }

    @Test
    void isExcelFile_shouldReturnFalse_whenNullOrBlank() {
        assertFalse(uploadService.isExcelFile(""));
        assertFalse(uploadService.isExcelFile(null));
    }
}
