package com.rapidminer.homework.service.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ConverterService {
    private static final Logger LOG = LoggerFactory.getLogger(ConverterService.class);

    @Async
    public CompletableFuture<Boolean> convert(UUID id, String fileName) {
        LOG.debug("Starting converting of file with id: '{}', name: '{}'...", id, fileName);

        // TODO: Do conversion!
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            throw new ConversionException("Failed to convert file with name: " + fileName + ".", e);
        }

        LOG.debug("Successfully converted file with id: '{}', name: '{}'...", id, fileName);

        return CompletableFuture.completedFuture(true);
    }
}
