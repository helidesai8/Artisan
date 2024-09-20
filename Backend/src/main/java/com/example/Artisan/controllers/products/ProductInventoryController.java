package com.example.Artisan.controllers.products;

import com.example.Artisan.DTOs.products.ProductInventoryDTO;
import com.example.Artisan.services.products.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products/")
public class ProductInventoryController {

    private final ProductInventoryService productInventoryService;

    @Autowired
    public ProductInventoryController(ProductInventoryService productInventoryService) {
        this.productInventoryService = productInventoryService;
    }

    /**
     * Updates the inventory of a product.
     *
     * @param productId   The ID of the product.
     * @param inventoryDTO The DTO containing the updated inventory quantity.
     * @return A ResponseEntity with HTTP status OK if the inventory is updated successfully,
     *         or a ResponseEntity with HTTP status BAD_REQUEST if an error occurs.
     */
    @PutMapping("{productId}/inventory")
    public ResponseEntity<?> updateProductInventory(@PathVariable Long productId, @RequestBody ProductInventoryDTO inventoryDTO) {
        try {
            productInventoryService.updateInventory(productId, inventoryDTO.getQuantity());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves the inventory quantity of a product.
     *
     * @param productId The ID of the product.
     * @return A ResponseEntity with HTTP status OK and the inventory quantity as the response body,
     *         or a ResponseEntity with HTTP status NOT_FOUND if the inventory information is not found,
     *         or a ResponseEntity with HTTP status BAD_REQUEST if an error occurs.
     */
    @GetMapping("{productId}/inventory")
    public ResponseEntity<?> getProductInventory(@PathVariable Long productId) {
        try {
            Integer inventoryQuantity = productInventoryService.getInventoryByProductId(productId);
            if (inventoryQuantity != null) {
                return ResponseEntity.ok(inventoryQuantity);
            } else {
                return new ResponseEntity<>("Inventory information not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
