package com.example.Artisan.services.products;

import com.example.Artisan.DTOs.products.ProductStoryDTO;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.products.ProductStory;
import com.example.Artisan.exceptions.ProductNotFoundException;
import com.example.Artisan.repositories.products.ProductRepository;
import com.example.Artisan.repositories.products.ProductStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductStoryServiceTest {

    @Mock
    private ProductStoryRepository productStoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductStoryService productStoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveOrUpdateStory_productExists_storySaved() {
        Product product = new Product();
        ProductStoryDTO storyDTO = new ProductStoryDTO();
        ProductStory productStory = new ProductStory();
        when(productService.getProductById(1L)).thenReturn(product);
        when(productStoryRepository.findById(1L)).thenReturn(Optional.of(productStory));
        when(modelMapper.map(storyDTO, ProductStory.class)).thenReturn(productStory);
        when(modelMapper.map(productStory, ProductStoryDTO.class)).thenReturn(storyDTO);
        ProductStoryDTO result = productStoryService.saveOrUpdateStory(1L, storyDTO);
        assertEquals(storyDTO, result);
        verify(productStoryRepository, times(1)).save(productStory);
    }

    @Test
    public void saveOrUpdateStory_productDoesNotExist_storySaved() {
        ProductStoryDTO storyDTO = new ProductStoryDTO();
        ProductStory productStory = new ProductStory();
        when(productService.getProductById(1L)).thenReturn(null);
        when(productStoryRepository.findById(1L)).thenReturn(Optional.empty());
        when(modelMapper.map(storyDTO, ProductStory.class)).thenReturn(productStory);
        when(modelMapper.map(productStory, ProductStoryDTO.class)).thenReturn(storyDTO);
        ProductStoryDTO result = productStoryService.saveOrUpdateStory(1L, storyDTO);
        assertEquals(storyDTO, result);
        verify(productStoryRepository, times(1)).save(productStory);
    }

    @Test
    public void getStory_storyExists_storyReturned() {
        ProductStory productStory = new ProductStory();
        ProductStoryDTO storyDTO = new ProductStoryDTO();
        when(productStoryRepository.findById(1L)).thenReturn(Optional.of(productStory));
        when(modelMapper.map(productStory, ProductStoryDTO.class)).thenReturn(storyDTO);
        ProductStoryDTO result = productStoryService.getStory(1L);
        assertEquals(storyDTO, result);
    }

    @Test
    public void getStory_storyDoesNotExist_exceptionThrown() {
        when(productStoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productStoryService.getStory(1L));
    }

    @Test
    public void deleteStory_storyExists_storyDeleted() {
        when(productStoryRepository.existsById(1L)).thenReturn(true);
        productStoryService.deleteStory(1L);
        verify(productStoryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteStory_storyDoesNotExist_exceptionThrown() {
        when(productStoryRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> productStoryService.deleteStory(1L));
    }
}