package com.example.Artisan.controllers.products;

import com.example.Artisan.ArtisanApplication;
import com.example.Artisan.entities.products.ProductCategory;
import com.example.Artisan.services.products.ProductCategoryService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ArtisanApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ProductCategoryService productCategoryService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAllProductCategories_returnsAllProductCategories() throws Exception {
        ProductCategory category1 = new ProductCategory(1L, "Category 1", "Description 1", new HashSet<>());
        ProductCategory category2 = new ProductCategory(2L, "Category 2", "Description 2", new HashSet<>());
        List<ProductCategory> allProductCategories = Arrays.asList(category1, category2);

        given(productCategoryService.getAllProductCategories()).willReturn(allProductCategories);

        mockMvc.perform(get("/api/v1/products/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(category1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(category1.getName())))
                .andExpect(jsonPath("$[1].id", is(category2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(category2.getName())));
    }

    @Test
    public void addProductCategory_createsProductCategory() throws Exception {
        ProductCategory newProductCategory = new ProductCategory(1L, "Category 1", "Description 1", new HashSet<>());
        given(productCategoryService.saveProductCategory(any(ProductCategory.class))).willReturn(newProductCategory);

        mockMvc.perform(post("/api/v1/products/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Home Appliances\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(newProductCategory.getId().intValue())))
                .andExpect(jsonPath("$.name", is(newProductCategory.getName())));
    }

    @Test
    public void getProductCategoryById_returnsProductCategory() throws Exception {
        ProductCategory category = new ProductCategory(1L, "Category 1", "Description 1", new HashSet<>());
        given(productCategoryService.getProductCategoryById(1L)).willReturn(category);

        mockMvc.perform(get("/api/v1/products/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(category.getId().intValue())))
                .andExpect(jsonPath("$.name", is(category.getName())));
    }

    @Test
    public void updateProductCategory_updatesProductCategory() throws Exception {
        ProductCategory updatedCategory = new ProductCategory(1L, "Updated Category", "Updated Description", new HashSet<>());
        given(productCategoryService.updateProductCategory(any(Long.class), any(ProductCategory.class))).willReturn(updatedCategory);

        mockMvc.perform(put("/api/v1/products/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics and Gadgets\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedCategory.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedCategory.getName())));
    }

    @Test
    public void deleteProductCategory_deletesProductCategory() throws Exception {
        doNothing().when(productCategoryService).deleteProductCategory(1L);

        mockMvc.perform(delete("/api/v1/products/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}