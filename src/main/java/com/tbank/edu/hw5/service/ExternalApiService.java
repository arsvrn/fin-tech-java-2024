package com.tbank.edu.hw5.service;

import com.tbank.edu.hw5.model.Category;
import com.tbank.edu.hw5.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Category> fetchCategories() {
        Category[] categories = restTemplate.getForObject("https://kudago.com/public-api/v1.4/place-categories", Category[].class);
        return Arrays.asList(categories);
    }

    public List<Location> fetchLocations() {
        Location[] locations = restTemplate.getForObject("https://kudago.com/public-api/v1.4/locations", Location[].class);
        return Arrays.asList(locations);
    }
}
