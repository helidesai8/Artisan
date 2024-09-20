package com.example.Artisan.controllers.products;

import com.example.Artisan.DTOs.products.ProductInventoryDTO;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.products.ProductInventory;
import com.example.Artisan.exceptions.ProductNotFoundException;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.repositories.products.ProductInventoryRepository;
import com.example.Artisan.repositories.products.ProductRepository;
import com.example.Artisan.services.products.ProductInventoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductInventoryControllerTest {

    @Mock
    private ProductInventoryService productInventoryService;
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductInventoryRepository productInventoryRepository;


    @InjectMocks
    private ProductInventoryController productInventoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateProductInventoryReturnsOkWhenSuccessful() {
        doNothing().when(productInventoryService).updateInventory(anyLong(), any());

        ResponseEntity<?> response = productInventoryController.updateProductInventory(1L, new ProductInventoryDTO());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateProductInventoryReturnsBadRequestWhenUnsuccessful() {
        doThrow(new RuntimeException("Error")).when(productInventoryService).updateInventory(anyLong(), any());

        ResponseEntity<?> response = productInventoryController.updateProductInventory(1L, new ProductInventoryDTO());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateInventory_productExists_inventoryNotNull() {
        Product product = new Product();
        ProductInventory inventory = new ProductInventory();
        product.setProductInventory(inventory);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productInventoryController.updateProductInventory(1L, new ProductInventoryDTO(null, 10));

        verify(productInventoryService, times(1)).updateInventory(1L, 10);
    }

    @Test
    public void updateInventory_productExists_inventoryNull() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productInventoryController.updateProductInventory(1L, new ProductInventoryDTO(null, 10));

        verify(productInventoryService, times(1)).updateInventory(1L, 10);
    }

    @Test
    void getProductInventoryReturnsOkWhenInventoryExists() {
        when(productInventoryService.getInventoryByProductId(anyLong())).thenReturn(10);

        ResponseEntity<?> response = productInventoryController.getProductInventory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getProductInventoryReturnsNotFoundWhenInventoryDoesNotExist() {
        when(productInventoryService.getInventoryByProductId(anyLong())).thenReturn(null);

        ResponseEntity<?> response = productInventoryController.getProductInventory(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getProductInventoryReturnsBadRequestWhenUnsuccessful() {
        when(productInventoryService.getInventoryByProductId(anyLong())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = productInventoryController.getProductInventory(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}