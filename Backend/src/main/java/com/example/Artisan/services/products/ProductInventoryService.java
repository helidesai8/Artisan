package com.example.Artisan.services.products;

import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.products.ProductInventory;
import com.example.Artisan.exceptions.ProductNotFoundException;
import com.example.Artisan.repositories.products.ProductInventoryRepository;
import com.example.Artisan.repositories.products.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInventoryService {

    private final ProductRepository productRepository;
    private final ProductInventoryRepository productInventoryRepository;

    /**
     * Constructs a new ProductInventoryService with the specified repositories.
     *
     * @param productRepository The repository for accessing product data.
     * @param productInventoryRepository The repository for accessing product inventory data.
     */
    @Autowired
    public ProductInventoryService(ProductRepository productRepository, ProductInventoryRepository productInventoryRepository) {
        this.productRepository = productRepository;
        this.productInventoryRepository = productInventoryRepository;
    }

    /**
     * Updates the inventory quantity for a product.
     *
     * @param productId The ID of the product.
     * @param quantity The new quantity for the product inventory.
     * @throws ProductNotFoundException If the product with the specified ID is not found.
     */
    public void updateInventory(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        ProductInventory inventory = product.getProductInventory();
        if (inventory == null) {
            inventory = new ProductInventory();
            inventory.setProduct(product);
            inventory.setArtist(product.getArtist());
        }
        inventory.setQuantity(quantity);
        productInventoryRepository.save(inventory);
    }

    /**
     * Retrieves the inventory quantity for a product.
     *
     * @param productId The ID of the product.
     * @return The inventory quantity of the product. Returns 0 if the product inventory is not found.
     * @throws ProductNotFoundException If the product with the specified ID is not found.
     */
    public Integer getInventoryByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        ProductInventory inventory = product.getProductInventory();
        if (inventory != null) {
            return inventory.getQuantity();
        } else {
            return 0;
        }
    }
}
