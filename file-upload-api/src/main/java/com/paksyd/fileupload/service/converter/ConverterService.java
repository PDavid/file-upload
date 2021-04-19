package com.paksyd.fileupload.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paksyd.fileupload.model.FileContentResponse;
import com.paksyd.fileupload.model.Row;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ConverterService {
    private static final Logger LOG = LoggerFactory.getLogger(ConverterService.class);

    // TODO: Move this to config!
    private final Path fileStorageLocation = Path.of("upload");

    @Async
    public CompletableFuture<Boolean> convert(UUID id, String fileName) {
        LOG.debug("Starting converting of file with id: '{}', name: '{}'...", id, fileName);

        convertFile(id, fileName);

        LOG.debug("Successfully converted file with id: '{}', name: '{}'...", id, fileName);

        return CompletableFuture.completedFuture(true);
    }

    private void convertFile(UUID id, String fileName) {

        FileContentResponse fileContentResponse = convertExcelFile(id, fileName);

        LOG.debug("fileContentResponse: {}", fileContentResponse);

        String jsonFileName = fileName
                .replaceAll(".xlsx", ".json");

        Path directory = this.fileStorageLocation.resolve(id.toString());

        Path targetLocation = directory.resolve(jsonFileName);

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(targetLocation.toFile(), fileContentResponse);
        } catch (IOException e) {
            throw new ConversionException("Failed to convert file with name: " + fileName + ".", e);
        }

        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            throw new ConversionException("Failed to convert file with name: " + fileName + ".", e);
        }
    }

    private FileContentResponse convertExcelFile(UUID id, String fileName) {
        Path directory = this.fileStorageLocation.resolve(id.toString());

        Path excelFile = directory.resolve(fileName);

        LOG.debug("Opening workbook: {}...", excelFile);

        try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile.toFile())) {

            LOG.debug("Successfully opened workbook: {}", excelFile);

            XSSFSheet sheet = workbook.getSheetAt(0);

            List<Row> rows = new ArrayList<>();
            sheet.forEach(row -> {

                List<String> columns = new ArrayList<>();

                row.forEach(cell -> {
                    String cellValue = getCellValue(cell);
                    columns.add(cellValue);
                });

                rows.add(new Row(columns));
            });

            return new FileContentResponse(rows);

        } catch (InvalidFormatException | IOException e) {
            throw new ConversionException("Failed to convert file with name: " + fileName + ".", e);
        }
    }

    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }

    }
}