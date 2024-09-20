package com.example.Artisan.controllers.products;

import com.example.Artisan.DTOs.products.ProductStoryDTO;
import com.example.Artisan.services.products.ProductStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/products/{productId}/story")
public class ProductStoryController {

    private final ProductStoryService productStoryService;

    @Autowired
    public ProductStoryController(ProductStoryService productStoryService) {
        this.productStoryService = productStoryService;
    }

    /**
     * Add a new story for a product.
     *
     * @param productId the ID of the product
     * @param story     the story to be added
     * @return the saved story as a ProductStoryDTO
     */
    @PostMapping
    public ResponseEntity<ProductStoryDTO> addStory(@PathVariable Long productId, @RequestBody String story) {
        ProductStoryDTO storyDTO = new ProductStoryDTO();
        storyDTO.setProductId(productId); // Assuming constructor or setter methods are available
        storyDTO.setStory(story);
        ProductStoryDTO savedStory = productStoryService.saveOrUpdateStory(productId, storyDTO);
        return ResponseEntity.ok(savedStory);
    }

    /**
     * Get the story of a product.
     *
     * @param productId the ID of the product
     * @return the story of the product as a ProductStoryDTO
     */
    @GetMapping
    public ResponseEntity<ProductStoryDTO> getStory(@PathVariable Long productId) {
        ProductStoryDTO storyDTO = productStoryService.getStory(productId);
        return ResponseEntity.ok(storyDTO);
    }

    /**
     * Update the story of a product.
     *
     * @param productId the ID of the product
     * @param story     the updated story
     * @return the updated story as a ProductStoryDTO
     */
    @PutMapping
    public ResponseEntity<ProductStoryDTO> updateStory(@PathVariable Long productId, @RequestBody String story) {
        ProductStoryDTO storyDTO = new ProductStoryDTO();
        storyDTO.setProductId(productId);
        storyDTO.setStory(story);
        ProductStoryDTO updatedStory = productStoryService.saveOrUpdateStory(productId, storyDTO);
        return ResponseEntity.ok(updatedStory);
    }

    /**
     * Delete the story of a product.
     *
     * @param productId the ID of the product
     * @return a ResponseEntity with no content
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteStory(@PathVariable Long productId) {
        productStoryService.deleteStory(productId);
        return ResponseEntity.noContent().build();
    }
}
