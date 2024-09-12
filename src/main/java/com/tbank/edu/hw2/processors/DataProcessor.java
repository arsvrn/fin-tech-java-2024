package com.tbank.edu.hw2.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@AllArgsConstructor
public class DataProcessor<T> {

    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    public DataProcessor() {
        this.objectMapper = new ObjectMapper();
        this.xmlMapper = new XmlMapper();
    }

    public T readJson(String filePath, Class<T> clazz) throws IOException {
        log.info("Starting to read JSON from a file: {}", filePath);
        try {
            T result = objectMapper.readValue(new File(filePath), clazz);
            log.debug("JSON has been successfully read and an object has been created: {}", result);
            return result;
        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error("File reading error: {}", e.getMessage());
            throw e;
        }
    }

    public String toXML(T object) throws JsonProcessingException {
        log.info("Converting an object to XML");
        try {
            String xml = xmlMapper.writeValueAsString(object);
            log.debug("The conversion is successful: {}", xml);
            return xml;
        } catch (JsonProcessingException e) {
            log.error("Error converting to XML: {}", e.getMessage());
            throw e;
        }
    }

    public void saveToFile(String data, String filePath) throws IOException {
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
            throw e;
        }
    }
}
