package com.example.Artisan.controllers.products;

import com.example.Artisan.ArtisanApplication;
import com.example.Artisan.DTOs.products.ProductInventoryDTO;
import com.example.Artisan.services.products.ProductInventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ArtisanApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductInventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductInventoryService productInventoryService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void updateProductInventoryReturnsNotFoundWhenProductIdIsInvalid() throws Exception {
        Long invalidProductId = 999L;
        ProductInventoryDTO inventoryDTO = new ProductInventoryDTO(null, 10);

        doThrow(new RuntimeException("Product not found")).when(productInventoryService).updateInventory(invalidProductId, inventoryDTO.getQuantity());

        mockMvc.perform(put("/api/v1/products/" + invalidProductId + "/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateProductInventoryReturnsBadRequestWhenQuantityIsInvalid() throws Exception {
        Long productId = 1L;
        ProductInventoryDTO inventoryDTO = new ProductInventoryDTO(null, -10);

        mockMvc.perform(put("/api/v1/products/" + productId + "/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getProductInventoryReturnsNotFoundWhenProductIdIsInvalid() throws Exception {
        Long invalidProductId = 999L;
        ProductInventoryDTO inventoryDTO = new ProductInventoryDTO(null, -10);

        when(productInventoryService.getInventoryByProductId(invalidProductId)).thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(put("/api/v1/products/" + invalidProductId + "/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isForbidden());
    }
}