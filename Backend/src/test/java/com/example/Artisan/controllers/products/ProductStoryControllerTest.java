package com.example.Artisan.controllers.products;

import com.example.Artisan.DTOs.products.ProductStoryDTO;
import com.example.Artisan.services.products.ProductStoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ProductStoryControllerTest {

    @Mock
    private ProductStoryService productStoryService;

    @InjectMocks
    private ProductStoryController productStoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addStoryReturnsOkWhenSuccessful() {
        when(productStoryService.saveOrUpdateStory(anyLong(), any())).thenReturn(new ProductStoryDTO());

        ResponseEntity<ProductStoryDTO> response = productStoryController.addStory(1L, "story");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getStoryReturnsOkWhenStoryExists() {
        when(productStoryService.getStory(anyLong())).thenReturn(new ProductStoryDTO());

        ResponseEntity<ProductStoryDTO> response = productStoryController.getStory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateStoryReturnsOkWhenSuccessful() {
        when(productStoryService.saveOrUpdateStory(anyLong(), any())).thenReturn(new ProductStoryDTO());

        ResponseEntity<ProductStoryDTO> response = productStoryController.updateStory(1L, "story");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteStoryReturnsNoContentWhenSuccessful() {
        ResponseEntity<Void> response = productStoryController.deleteStory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}