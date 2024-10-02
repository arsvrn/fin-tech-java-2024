package com.tbank.edu.hw5.service;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.model.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ExternalApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_CATEGORIES = "https://kudago.com/public-api/v1.4/place-categories";
    private final String URL_LOCATIONS = "https://kudago.com/public-api/v1.4/locations";

    public List<Category> fetchCategoriesOrNull() {
        try {
            Category[] categories = restTemplate.getForObject(URL_CATEGORIES, Category[].class);
            return categories != null ? Arrays.asList(categories) : Collections.emptyList();
        } catch (RestClientException e) {

            log.error("Ошибка при получении категорий: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Location> fetchLocationsOrNull() {
        try {
            Location[] locations = restTemplate.getForObject(URL_LOCATIONS, Location[].class);
            return locations != null ? Arrays.asList(locations) : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("Ошибка при получении локаций: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
