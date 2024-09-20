package com.example.Artisan.services.products;

import com.example.Artisan.entities.products.ProductCategory;
import com.example.Artisan.exceptions.ProductCategoryNotFoundException;
import com.example.Artisan.repositories.products.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductCategoryService productCategoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveProductCategory_success() {
        ProductCategory productCategory = new ProductCategory();
        when(productCategoryRepository.save(productCategory)).thenReturn(productCategory);
        ProductCategory savedProductCategory = productCategoryService.saveProductCategory(productCategory);
        assertEquals(productCategory, savedProductCategory);
    }

    @Test
    public void getAllProductCategories_returnsList() {
        ProductCategory productCategory1 = new ProductCategory();
        ProductCategory productCategory2 = new ProductCategory();
        when(productCategoryRepository.findAll()).thenReturn(Arrays.asList(productCategory1, productCategory2));
        List<ProductCategory> productCategories = productCategoryService.getAllProductCategories();
        assertEquals(2, productCategories.size());
    }

    @Test
    public void getProductCategoryById_found() {
        ProductCategory productCategory = new ProductCategory();
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(productCategory));
        ProductCategory foundProductCategory = productCategoryService.getProductCategoryById(1L);
        assertEquals(productCategory, foundProductCategory);
    }

    @Test
    public void getProductCategoryById_notFound() {
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductCategoryNotFoundException.class, () -> productCategoryService.getProductCategoryById(1L));
    }

    @Test
    public void updateProductCategory_success() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Old Name");
        ProductCategory updatedProductCategory = new ProductCategory();
        updatedProductCategory.setName("New Name");
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(productCategory));
        when(productCategoryRepository.save(productCategory)).thenReturn(updatedProductCategory);
        ProductCategory result = productCategoryService.updateProductCategory(1L, updatedProductCategory);
        assertEquals("New Name", result.getName());
    }

    @Test
    public void deleteProductCategory_success() {
        doNothing().when(productCategoryRepository).deleteById(1L);
        productCategoryService.deleteProductCategory(1L);
        verify(productCategoryRepository, times(1)).deleteById(1L);
    }
}