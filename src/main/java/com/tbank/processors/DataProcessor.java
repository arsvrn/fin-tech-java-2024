package com.tbank.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class DataProcessor<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public T readJson(String filePath, Class<T> clazz) {
        log.info("Starting to read JSON from a file: {}", filePath);
        try {
            T result = objectMapper.readValue(new File(filePath), clazz);
            log.debug("JSON has been successfully read and an object has been created: {}", result);
            return result;
        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            return null;
        } catch (IOException e) {
            log.error("File reading error: {}", e.getMessage());
            return null;
        }
    }

    public String toXML(T object) {
        log.info("Converting an object to XML");
        XmlMapper xmlMapper = new XmlMapper();
        try {
            String xml = xmlMapper.writeValueAsString(object);
            log.debug("The conversion is successful: {}", xml);
            return xml;
        } catch (JsonProcessingException e) {
            log.error("Error converting to XML: {}", e.getMessage());
            return null;
        }
    }

    public void saveToFile(String data, String filePath) {
        log.info("Saving data to a file: {}", filePath);
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                boolean created = file.createNewFile();
                log.debug("The file has been created: {}", created);
            }
            Files.writeString(file.toPath(), data);
            log.info("The data has been successfully saved to a file: {}", filePath);
        } catch (IOException e) {
            log.error("Error saving to file: {}", e.getMessage());
        }
    }
}
