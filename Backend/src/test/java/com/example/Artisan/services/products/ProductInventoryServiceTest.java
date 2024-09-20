package com.example.Artisan.services.products;

import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.products.ProductInventory;
import com.example.Artisan.exceptions.ProductNotFoundException;
import com.example.Artisan.repositories.products.ProductInventoryRepository;
import com.example.Artisan.repositories.products.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductInventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductInventoryRepository productInventoryRepository;

    @InjectMocks
    private ProductInventoryService productInventoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updateInventory_productExists_inventoryUpdated() {
        Product product = new Product();
        ProductInventory inventory = new ProductInventory();
        product.setProductInventory(inventory);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productInventoryService.updateInventory(1L, 10);
        assertEquals(10, inventory.getQuantity());
        verify(productInventoryRepository, times(1)).save(inventory);
    }

    @Test
    public void updateInventory_productDoesNotExist_exceptionThrown() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productInventoryService.updateInventory(1L, 10));
    }

    @Test
    public void getInventoryByProductId_productExists_inventoryReturned() {
        Product product = new Product();
        ProductInventory inventory = new ProductInventory();
        inventory.setQuantity(10);
        product.setProductInventory(inventory);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Integer quantity = productInventoryService.getInventoryByProductId(1L);
        assertEquals(10, quantity);
    }

    @Test
    public void getInventoryByProductId_productDoesNotExist_exceptionThrown() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productInventoryService.getInventoryByProductId(1L));
    }

    @Test
    public void getInventoryByProductId_productExistsNoInventory_zeroReturned() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Integer quantity = productInventoryService.getInventoryByProductId(1L);
        assertEquals(0, quantity);
    }
}