package com.example.Artisan.controllers.products;

import com.example.Artisan.controllers.products.ProductCategoryController;
import com.example.Artisan.entities.products.ProductCategory;
import com.example.Artisan.services.products.ProductCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductCategoryControllerTest {

    @Mock
    private ProductCategoryService productCategoryService;

    private ProductCategoryController productCategoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productCategoryController = new ProductCategoryController(productCategoryService);
    }

    @Test
    void addProductCategory_returnsNewProductCategory() {
        ProductCategory productCategory = new ProductCategory();
        when(productCategoryService.saveProductCategory(productCategory)).thenReturn(productCategory);

        ResponseEntity<ProductCategory> response = productCategoryController.addProductCategory(productCategory);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(productCategory, response.getBody());
    }

    @Test
    void getAllProductCategories_returnsAllProductCategories() {
        List<ProductCategory> productCategories = Arrays.asList(new ProductCategory(), new ProductCategory());
        when(productCategoryService.getAllProductCategories()).thenReturn(productCategories);

        ResponseEntity<List<ProductCategory>> response = productCategoryController.getAllProductCategories();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productCategories, response.getBody());
    }

    @Test
    void getProductCategoryById_returnsProductCategory() {
        ProductCategory productCategory = new ProductCategory();
        when(productCategoryService.getProductCategoryById(1L)).thenReturn(productCategory);

        ResponseEntity<ProductCategory> response = productCategoryController.getProductCategoryById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productCategory, response.getBody());
    }

    @Test
    void updateProductCategory_returnsUpdatedProductCategory() {
        ProductCategory productCategory = new ProductCategory();
        when(productCategoryService.updateProductCategory(1L, productCategory)).thenReturn(productCategory);

        ResponseEntity<ProductCategory> response = productCategoryController.updateProductCategory(1L, productCategory);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productCategory, response.getBody());
    }

    @Test
    void deleteProductCategory_returnsNoContent() {
        doNothing().when(productCategoryService).deleteProductCategory(1L);

        ResponseEntity<Void> response = productCategoryController.deleteProductCategory(1L);

        assertEquals(204, response.getStatusCodeValue());
    }
}
