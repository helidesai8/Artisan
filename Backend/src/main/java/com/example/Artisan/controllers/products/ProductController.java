package com.example.Artisan.controllers.products;

import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.services.products.ProductImageService;
import com.example.Artisan.services.products.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for handling product-related API endpoints.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ProductImageService productImageService;

    /**
     * Constructor for ProductController.
     * 
     * @param productService     The ProductService instance.
     * @param artistRepository   The ArtistRepository instance.
     * @param productImageService The ProductImageService instance.
     */
    @Autowired
    public ProductController(ProductService productService, ArtistRepository artistRepository, ProductImageService productImageService) {
        this.productService = productService;
        this.artistRepository = artistRepository;
        this.productImageService = productImageService;
    }

    /**
     * Endpoint for adding a new product.
     * 
     * @param productJson The JSON representation of the product.
     * @param images      The list of product images.
     * @return The ResponseEntity containing the created ProductDTO.
     * @throws IOException if there is an error reading the product JSON.
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> addProduct(@RequestPart("product") String productJson,
                                                 @RequestPart("imageUrls") List<MultipartFile> images) throws IOException {
        ProductDTO productDTO = new ObjectMapper().readValue(productJson, ProductDTO.class);
        Long artistId = extractArtistIdFromSecurityContext();
        List<String> imageUrls = productImageService.uploadImages(images);
        productDTO.setImageUrls(imageUrls);
        ProductDTO newProduct = productService.saveProduct(productDTO, artistId);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    /**
     * Extracts the artist ID from the security context.
     * 
     * @return The artist ID.
     */
    private Long extractArtistIdFromSecurityContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Artist artist = artistRepository.findByEmail(email);
        return artist.getArtistId();
    }

    /**
     * Endpoint for retrieving all products.
     * 
     * @return The ResponseEntity containing the list of all ProductDTOs.
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        var allProducts = productService.getAllProducts();
        List<ProductDTO> products = allProducts.stream()
                .map(productService::mapProductWithImagesAndCategories) // Convert each Product to ProductDTO
                .toList();
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint for retrieving a product by ID.
     * 
     * @param id The ID of the product.
     * @return The ResponseEntity containing the ProductDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        ProductDTO productDTO = productService.mapProductWithImagesAndCategories(productService.getProductById(id));
        return ResponseEntity.ok(productDTO);
    }

    /**
     * Endpoint for updating a product.
     * 
     * @param id          The ID of the product to update.
     * @param productJson The JSON representation of the updated product.
     * @param images      The list of updated product images.
     * @return The ResponseEntity containing the updated ProductDTO.
     * @throws IOException if there is an error reading the product JSON.
     */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id,
                                                    @RequestPart("product") String productJson,
                                                    @RequestPart(name = "images", required = false) List<MultipartFile> images)
            throws IOException {
        ProductDTO productDTO = new ObjectMapper().readValue(productJson, ProductDTO.class);
        List<String> imageUrls = null;
        if (images != null && !images.isEmpty()) {
            imageUrls = productImageService.uploadImages(images);
            productDTO.setImageUrls(imageUrls);
        }
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO, imageUrls);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Endpoint for deleting a product.
     * 
     * @param id The ID of the product to delete.
     * @return The ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for retrieving products by the currently logged-in artist.
     * 
     * @return The ResponseEntity containing the list of ProductDTOs.
     */
    @GetMapping("/artist")
    public ResponseEntity<List<ProductDTO>> getProductsByArtist() {
        Long artist_id = extractArtistIdFromSecurityContext();
        List<ProductDTO> products = productService.getProductsByArtistId(artist_id);
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint for retrieving products by artist ID.
     * 
     * @param artist_id The ID of the artist.
     * @return The ResponseEntity containing the list of ProductDTOs.
     */
    @GetMapping("/artist/{id}")
    public ResponseEntity<List<ProductDTO>> getProductsByArtistId(@PathVariable("id") Long artist_id) {
        List<ProductDTO> products = productService.getProductsByArtistId(artist_id);
        return ResponseEntity.ok(products);
    }
}
