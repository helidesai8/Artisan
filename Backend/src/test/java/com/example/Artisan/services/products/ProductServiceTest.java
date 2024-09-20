package com.example.Artisan.services.products;

import com.example.Artisan.DTOs.products.CheckoutProductDTO;
import com.example.Artisan.DTOs.products.ProductBaseDTO;
import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.products.ProductImage;
import com.example.Artisan.entities.products.ProductInventory;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.exceptions.ArtistNotFoundException;
import com.example.Artisan.exceptions.ProductNotFoundException;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.repositories.products.ProductCategoryRepository;
import com.example.Artisan.repositories.products.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProductInventoryService productInventoryService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void saveProduct_artistExists_productSaved() {
        Artist artist = new Artist();
        artist.setArtistId(1L);

        ProductDTO productDTO = new ProductDTO();
        Product product = new Product();
        product.setArtist(artist);
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDTO result = productService.saveProduct(productDTO, 1L);
        ProductDTO expected = new ProductDTO();
        expected.setImageUrls(new ArrayList<>());
        expected.setQuantity(0);
        expected.setIsActive(true);
        expected.setArtistId(1);
        assertEquals(expected, result);
    }

    @Test
     void saveProduct_artistDoesNotExist_exceptionThrown() {
        ProductDTO productDTO = new ProductDTO();
        when(artistRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ArtistNotFoundException.class, () -> productService.saveProduct(productDTO, 1L));
    }

    @Test
     void updateProduct_productExists_productUpdated() {
        Long id = 1L;
        ProductDTO productDTO = new ProductDTO();
        List<String> imageUrls = new ArrayList<>();
        Product product = new Product();
        Artist artist = new Artist();
        artist.setArtistId(1L);
        ProductInventory productInventory = new ProductInventory();
        product.setProductInventory(productInventory);
        product.setArtist(artist);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDTO result = productService.updateProduct(id, productDTO, imageUrls);
        ProductDTO expected = new ProductDTO();
        expected.setImageUrls(new ArrayList<>());
        expected.setIsActive(true);
        expected.setArtistId(1);
        assertEquals(expected, result);
    }

    @Test
     void updateProduct_productDoesNotExist_exceptionThrown() {
        Long id = 1L;
        ProductDTO productDTO = new ProductDTO();
        List<String> imageUrls = new ArrayList<>();
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(id, productDTO, imageUrls));
    }

    @Test
     void updateProduct_productExistsNoInventory_inventoryCreated() {
        Long id = 1L;
        ProductDTO productDTO = new ProductDTO();
        List<String> imageUrls = new ArrayList<>();
        Product product = new Product();
        var artist = new Artist();
        artist.setArtistId(1L);
        product.setArtist(artist);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDTO result = productService.updateProduct(id, productDTO, imageUrls);
        ProductDTO expected = new ProductDTO();
        expected.setImageUrls(new ArrayList<>());
        expected.setIsActive(true);
        expected.setArtistId(1L);
        assertEquals(expected, result);
    }

    @Test
     void updateProductImages_imagesExist_imagesUpdated() {
        Product product = new Product();
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");
        imageUrls.add("url2");
        productService.updateProductImages(product, imageUrls);
        assertEquals(2, product.getImageUrls().size());
    }

    @Test
     void updateProductImages_noImagesExist_imagesAdded() {
        Product product = new Product();
        List<String> imageUrls = new ArrayList<>();
        productService.updateProductImages(product, imageUrls);
        assertEquals(0, product.getImageUrls().size());
    }

    @Test
     void updateProductImages_imagesExist_imagesRemoved() {
        Product product = new Product();
        List<String> imageUrls = new ArrayList<>();
        ProductImage image1 = new ProductImage();
        image1.setUrl("url1");
        ProductImage image2 = new ProductImage();
        image2.setUrl("url2");
        product.getImageUrls().add(image1);
        product.getImageUrls().add(image2);
        productService.updateProductImages(product, imageUrls);
        assertEquals(0, product.getImageUrls().size());
    }


    @Test
     void deleteProduct_productExists_productDeleted() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.deleteProduct(1L);
    }


    @Test
     void getProductById_productExists_productReturned() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.getProductById(1L);
        assertEquals(product, result);
    }

    @Test
     void getProductById_productDoesNotExist_exceptionThrown() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
     void deleteProduct_productExists_productDeactivated() {
        Long productId = 1L;
        Product product = new Product();
        product.setIsActive(true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        productService.deleteProduct(productId);
        assertFalse(product.getIsActive());
        verify(productRepository).save(product);
    }

    @Test
     void deleteProduct_productDoesNotExist_exceptionThrown() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(productId));
    }

    @Test
     void deleteProduct_productExistsButInactive_productActivated() {
        Long productId = 1L;
        Product product = new Product();
        product.setIsActive(false);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        productService.deleteProduct(productId);
        assertTrue(product.getIsActive());
        verify(productRepository, times(1)).save(product);
    }

    @Test
     void checkoutProducts_checkoutList_empty_IllegalArgumentExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> productService.CheckoutProducts(List.of()));
    }

    @Test
     void checkoutProducts_checkoutList_null_IllegalArgumentExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> productService.CheckoutProducts(null));
    }

    @Test
     void checkoutProducts_invalidProduct_ProductNotFoundExceptionThrown() {
        // arrange
        List<ProductBaseDTO> products = new ArrayList<ProductBaseDTO>() {{
            add(new ProductBaseDTO(1L, 2));
        }};
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.CheckoutProducts(products));
    }

    @Test
     void checkoutProducts_orderQuantityIsMore_APIExceptionThrown() {
        // arrange
        Product product = new Product();
        product.setName("test product");
        List<ProductBaseDTO> products = new ArrayList<ProductBaseDTO>() {{
            add(new ProductBaseDTO(1L, 5));
        }};
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productInventoryService.getInventoryByProductId(1L)).thenReturn(2);

        //Act and assert
        assertThrows(APIException.class, () -> productService.CheckoutProducts(products), "Quantity of product test product not available");
    }

    @Test
     void checkoutProducts_multipleProducts_ReturnCheckoutDetailList() {
        // arrange
        Product product1 = new Product();
        Product product2 = new Product();
        product1.setId(1L);
        product2.setId(2L);
        List<ProductBaseDTO> products = new ArrayList<ProductBaseDTO>() {{
            add(new ProductBaseDTO(1L, 2));
            add(new ProductBaseDTO(2L, 3));
        }};
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(modelMapper.map(product1, CheckoutProductDTO.class)).thenReturn(new CheckoutProductDTO("test1", new BigDecimal("23.22"), "test_url"));
        when(modelMapper.map(product2, CheckoutProductDTO.class)).thenReturn(new CheckoutProductDTO("test2", new BigDecimal("23.22"), null));
        when(productInventoryService.getInventoryByProductId(1L)).thenReturn(5);
        when(productInventoryService.getInventoryByProductId(2L)).thenReturn(5);

        // Act
        var result = productService.CheckoutProducts(products);

        //Assert
        assertEquals(2, result.getProducts().size(), "checkout product returned");
    }

    @Test
     void checkoutProducts_calculatesTotalAmount_ReturnCorrectPrice() {
        // arrange
        Product product1 = new Product();
        Product product2 = new Product();
        product1.setId(1L);
        product2.setId(2L);
        product1.setPrice(new BigDecimal("22.00"));
        product2.setPrice(new BigDecimal("33.00"));
        List<ProductBaseDTO> products = new ArrayList<ProductBaseDTO>() {{
            add(new ProductBaseDTO(1L, 2));
            add(new ProductBaseDTO(2L, 3));
        }};

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(modelMapper.map(product1, CheckoutProductDTO.class)).thenReturn(new CheckoutProductDTO("test1", new BigDecimal("22.00"), "test_url"));
        when(modelMapper.map(product2, CheckoutProductDTO.class)).thenReturn(new CheckoutProductDTO("test2", new BigDecimal("33.00"), "test_url"));
        when(productInventoryService.getInventoryByProductId(1L)).thenReturn(5);
        when(productInventoryService.getInventoryByProductId(2L)).thenReturn(7);

        // Act
        var result = productService.CheckoutProducts(products);

        //Assert
        assertEquals(new BigDecimal("143.00"), result.getTotalAmount(), "total amount for products");
    }

    @Test
     void checkoutProducts_multipleProductImages_ReturnFirstImageUrl() {
        // arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(new BigDecimal("22.00"));
        var prodImage1 = new ProductImage();
        prodImage1.setUrl("testurl1");
        prodImage1.setId(34L);
        var prodImage2 = new ProductImage();
        prodImage2.setUrl("testurl2");
        prodImage2.setId(39L);
        Set<ProductImage> prodImages = new HashSet<>() {{
            add(prodImage1);
            add(prodImage2);
        }};
        product1.setImageUrls(prodImages);
        List<ProductBaseDTO> products = new ArrayList<ProductBaseDTO>() {{
            add(new ProductBaseDTO(1L, 2));
        }};

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(modelMapper.map(product1, CheckoutProductDTO.class)).thenReturn(new CheckoutProductDTO("test1", new BigDecimal("22.00"), "test_url"));
        when(productInventoryService.getInventoryByProductId(1L)).thenReturn(5);

        // Act
        var result = productService.CheckoutProducts(products);
        var firstProduct = result.getProducts().stream().findFirst().get();
        //Assert
        assertEquals("testurl1", firstProduct.getImg_url(), "first image for the product returned");
    }
}