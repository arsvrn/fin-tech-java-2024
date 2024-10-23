package com.tbank.edu.hw10.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbank.edu.hw10.entity.Place;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@SpringJUnitConfig
public class PlaceApiTest {

    @Container
    public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Place testLocation;

    @DynamicPropertySource
    static void properties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
    }

    @BeforeEach
    void setUp() {
        testLocation = new Place();
        testLocation.setName("Москва");
        testLocation.setSlug("msk");
    }

   @Test
    void testCreateLocation() throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLocation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Москва"))
                .andExpect(jsonPath("$.slug").value("msk"));

        mockMvc.perform(get("/api/v1/locations/msk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Москва"))
                .andExpect(jsonPath("$.slug").value("msk"));
    }

    @Test
    void testUpdateLocation() throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLocation)))
                .andExpect(status().isCreated());

        testLocation.setName("Новая Москва");
        mockMvc.perform(put("/api/v1/locations/msk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLocation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Новая Москва"));

        mockMvc.perform(get("/api/v1/locations/msk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Новая Москва"));
    }

     @Test
    void testGetExistingLocation() throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLocation)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/locations/msk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Москва"))
                .andExpect(jsonPath("$.slug").value("msk"));
    }

    @Test
    void testGetNonExistingLocation() throws Exception {
        mockMvc.perform(get("/api/v1/locations/nonexistent"))
                .andExpect(status().isNotFound());
    }
}