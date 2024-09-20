package com.example.Artisan.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.filter.CorsFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CorsConfigTest {

    @InjectMocks
    private CorsConfig corsConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void corsFilter_returnsCorsFilterWithCorrectConfiguration() {
        CorsFilter corsFilter = corsConfig.corsFilter();

        assertNotNull(corsFilter);
    }
}