package com.example.Artisan.controllers.products;

import com.example.Artisan.entities.products.ProductCategory;
import com.example.Artisan.services.products.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing product categories.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/products/categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    /**
     * Constructor for ProductCategoryController.
     *
     * @param productCategoryService The service for managing product categories.
     */
    @Autowired
    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    /**
     * Add a new product category.
     *
     * @param productCategory The product category to be added.
     * @return The added product category.
     */
    @PostMapping
    public ResponseEntity<ProductCategory> addProductCategory(@RequestBody ProductCategory productCategory) {
        ProductCategory newProductCategory = productCategoryService.saveProductCategory(productCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProductCategory);
    }

    /**
     * Get all product categories.
     *
     * @return A list of all product categories.
     */
    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllProductCategories() {
        List<ProductCategory> productCategories = productCategoryService.getAllProductCategories();
        return ResponseEntity.ok(productCategories);
    }


    /**
     * Get a product category by its ID.
     *
     * @param categoryId The ID of the product category.
     * @return The product category with the specified ID.
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ProductCategory> getProductCategoryById(@PathVariable Long categoryId) {
        ProductCategory productCategory = productCategoryService.getProductCategoryById(categoryId);
        return ResponseEntity.ok(productCategory);
    }

    /**
     * Update a product category.
     *
     * @param categoryId           The ID of the product category to be updated.
     * @param productCategoryDetails The updated details of the product category.
     * @return The updated product category.
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<ProductCategory> updateProductCategory(@PathVariable Long categoryId, @RequestBody ProductCategory productCategoryDetails) {
        ProductCategory updatedProductCategory = productCategoryService.updateProductCategory(categoryId, productCategoryDetails);
        return ResponseEntity.ok(updatedProductCategory);
    }

    /**
     * Delete a product category.
     *
     * @param categoryId The ID of the product category to be deleted.
     * @return A response indicating the success of the deletion.
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long categoryId) {
        productCategoryService.deleteProductCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
