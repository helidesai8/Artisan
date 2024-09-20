package com.example.Artisan.services.products;

import com.example.Artisan.DTOs.CheckoutDetailDTO;
import com.example.Artisan.DTOs.products.ProductBaseDTO;
import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.entities.products.Product;

import java.util.List;

public interface IProductService {
    /**
     * Saves a new product.
     *
     * @param productDTO The product data transfer object.
     * @param artistId   The ID of the artist associated with the product.
     * @return The saved product as a product data transfer object.
     */
    ProductDTO saveProduct(ProductDTO productDTO, Long artistId);

    /**
     * Updates an existing product.
     *
     * @param id         The ID of the product to update.
     * @param productDTO The updated product data transfer object.
     * @param imageUrls  The list of image URLs associated with the product.
     * @return The updated product as a product data transfer object.
     */
    ProductDTO updateProduct(Long id, ProductDTO productDTO, List<String> imageUrls);

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     */
    void deleteProduct(Long id);

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The retrieved product.
     */
    Product getProductById(Long id);

    /**
     * Retrieves all products.
     *
     * @return A list of all products.
     */
    List<Product> getAllProducts();

    /**
     * Retrieves products by artist ID.
     *
     * @param artistId The ID of the artist.
     * @return A list of products associated with the artist.
     */
    List<ProductDTO> getProductsByArtistId(Long artistId);

    /**
     * Performs checkout for a list of products.
     *
     * @param products The list of products to checkout.
     * @return The checkout details as a checkout detail data transfer object.
     */
    CheckoutDetailDTO CheckoutProducts(List<ProductBaseDTO> products);
}
