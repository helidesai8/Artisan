package com.example.Artisan.controllers.products;

import com.example.Artisan.ArtisanApplication;
import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.services.products.ProductImageService;
import com.example.Artisan.services.products.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ArtisanApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductImageService productImageService;

    @MockBean
    private ArtistRepository artistRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        Artist mockArtist = new Artist();
        mockArtist.setArtistId(1L);
        mockArtist.setEmail("artist@example.com");

        given(artistRepository.findByEmail(anyString())).willReturn(mockArtist);
    }

    @Test
    @WithMockUser
    public void getAllProductsTest() throws Exception {
        given(productService.getAllProducts()).willReturn(Arrays.asList(new Product(), new Product()));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser
    public void addProductTest() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("New Product");
        productDTO.setPrice(BigDecimal.valueOf(20.0));

        given(productService.saveProduct(any(ProductDTO.class), anyLong())).willReturn(productDTO);

        MockMultipartFile productPart = new MockMultipartFile("product", "", "application/json", "{\"name\":\"New Product\",\"price\":20.0}".getBytes());
        MockMultipartFile imagePart = new MockMultipartFile("imageUrls", "image.jpg", "image/jpeg", "image bytes".getBytes());

        mockMvc.perform(multipart("/api/v1/products")
                        .file(productPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(20.0));
    }

    @Test
    @WithMockUser
    public void deleteProductTest() throws Exception {
        Long productId = 1L;

        mockMvc.perform(delete("/api/v1/products/" + productId))
                .andExpect(status().isNoContent());
    }

}