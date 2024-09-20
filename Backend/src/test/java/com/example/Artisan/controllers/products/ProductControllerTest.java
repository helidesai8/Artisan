package com.example.Artisan.controllers.products;

import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.controllers.products.ProductController;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.services.products.ProductImageService;
import com.example.Artisan.services.products.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ProductImageService productImageService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupSecurityContext();
    }

    private void setupSecurityContext() {
        Artist artist = new Artist();
        artist.setArtistId(1L);
        artist.setEmail("artist@example.com");

        when(artistRepository.findByEmail("artist@example.com")).thenReturn(artist);

        Authentication authentication = new UsernamePasswordAuthenticationToken(artist, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllProductsTest() {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(new Product(), new Product()));
        when(productService.mapProductWithImagesAndCategories(any())).thenReturn(new ProductDTO());

        ResponseEntity<List<ProductDTO>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void addProductReturnsCreatedWhenSuccessful() throws Exception {
        String productJson = "{\"name\":\"product1\",\"description\":\"description1\",\"price\":100.0}";
        MockMultipartFile image = new MockMultipartFile("image", "filename.jpg", "text/plain", "some xml".getBytes());
        List<MultipartFile> images = Arrays.asList(image);

        when(productImageService.uploadImages(any())).thenReturn(Arrays.asList("url1"));
        when(productService.saveProduct(any(ProductDTO.class), anyLong())).thenReturn(new ProductDTO());

        ResponseEntity<ProductDTO> response = productController.addProduct(productJson, images);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void addProductReturnsCreatedWhenNoImagesProvided() throws Exception {
        String productJson = "{\"name\":\"product1\",\"description\":\"description1\",\"price\":100.0}";

        when(productService.saveProduct(any(ProductDTO.class), anyLong())).thenReturn(new ProductDTO());

        ResponseEntity<ProductDTO> response = productController.addProduct(productJson, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void addProductThrowsExceptionWhenImageUploadFails() throws Exception {
        String productJson = "{\"name\":\"product1\",\"description\":\"description1\",\"price\":100.0}";
        MockMultipartFile image = new MockMultipartFile("image", "filename.jpg", "text/plain", "some xml".getBytes());
        List<MultipartFile> images = Arrays.asList(image);

        when(productImageService.uploadImages(any())).thenThrow(new IOException("Error uploading images"));

        assertThrows(IOException.class, () -> {
            productController.addProduct(productJson, images);
        });
    }

    @Test
    void getProductByIdTest() {
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(new Product());
        when(productService.mapProductWithImagesAndCategories(any())).thenReturn(new ProductDTO());

        ResponseEntity<ProductDTO> response = productController.getProductById(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void updateProductReturnsOkWhenNoImagesProvided() throws Exception {
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("product1");

        when(productService.updateProduct(anyLong(), any(ProductDTO.class), anyList())).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.updateProduct(productId, new ObjectMapper().writeValueAsString(productDTO), null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void getProductsByArtistIdReturnsOkWhenProductsExist() {
        Long artistId = 1L;
        when(productService.getProductsByArtistId(artistId)).thenReturn(Arrays.asList(new ProductDTO(), new ProductDTO()));

        ResponseEntity<List<ProductDTO>> response = productController.getProductsByArtistId(artistId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getProductsByArtistIdReturnsOkWhenNoProductsExist() {
        Long artistId = 1L;
        when(productService.getProductsByArtistId(artistId)).thenReturn(new ArrayList<>());

        ResponseEntity<List<ProductDTO>> response = productController.getProductsByArtistId(artistId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }


    @Test
    void deleteProductTest() {
        Long productId = 1L;

        ResponseEntity<HttpStatus> response = productController.deleteProduct(productId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getProductsByArtistTest() {
        when(productService.getProductsByArtistId(anyLong())).thenReturn(Arrays.asList(new ProductDTO(), new ProductDTO()));

        ResponseEntity<List<ProductDTO>> response = productController.getProductsByArtist();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @AfterEach
    public void clear(){
        SecurityContextHolder.clearContext();
    }
}



