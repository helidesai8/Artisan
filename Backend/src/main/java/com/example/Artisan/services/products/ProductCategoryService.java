package com.example.Artisan.services.products;

import com.example.Artisan.entities.products.ProductCategory;
import com.example.Artisan.exceptions.ProductCategoryNotFoundException;
import com.example.Artisan.repositories.products.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing product categories.
 */
@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    /**
     * Saves a product category.
     *
     * @param productCategory The product category to be saved.
     * @return The saved product category.
     */
    public ProductCategory saveProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    /**
     * Retrieves all product categories.
     *
     * @return A list of all product categories.
     */
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    /**
     * Retrieves a product category by its ID.
     *
     * @param id The ID of the product category.
     * @return The product category with the specified ID.
     * @throws ProductCategoryNotFoundException if the product category is not found.
     */
    public ProductCategory getProductCategoryById(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));
    }

    /**
     * Updates a product category.
     *
     * @param id                   The ID of the product category to be updated.
     * @param productCategoryDetails The updated details of the product category.
     * @return The updated product category.
     * @throws ProductCategoryNotFoundException if the product category is not found.
     */
    public ProductCategory updateProductCategory(Long id, ProductCategory productCategoryDetails) {
        ProductCategory productCategory = getProductCategoryById(id);
        productCategory.setName(productCategoryDetails.getName());
        return productCategoryRepository.save(productCategory);
    }

    /**
     * Deletes a product category by its ID.
     *
     * @param id The ID of the product category to be deleted.
     */
    public void deleteProductCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }
}
