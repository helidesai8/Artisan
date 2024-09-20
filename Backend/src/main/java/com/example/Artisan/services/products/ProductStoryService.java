package com.example.Artisan.services.products;

import com.example.Artisan.DTOs.products.ProductStoryDTO;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.products.ProductStory;
import com.example.Artisan.exceptions.ProductNotFoundException;
import com.example.Artisan.repositories.products.ProductRepository;
import com.example.Artisan.repositories.products.ProductStoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductStoryService {

    @Autowired
    private final ProductStoryRepository productStoryRepository;
    @Autowired
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    private final ProductService productService;

    @Autowired
    public ProductStoryService(ProductStoryRepository productStoryRepository, ProductRepository productRepository, ModelMapper modelMapper, ProductService productService) {
        this.productStoryRepository = productStoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    public ProductStoryDTO saveOrUpdateStory(Long productId, ProductStoryDTO storyDTO) {

        Product product = productService.getProductById(productId);
        var productStory = productStoryRepository.findById(productId)
                .orElse(new ProductStory());

        modelMapper.map(storyDTO, productStory);
        productStory.setProduct(product);
        productStoryRepository.save(productStory);

        return modelMapper.map(productStory, ProductStoryDTO.class);
    }

    public ProductStoryDTO getStory(Long productId) {
        var productStory = productStoryRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return modelMapper.map(productStory, ProductStoryDTO.class);
    }

    public void deleteStory(Long productId) {
        if (!productStoryRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productStoryRepository.deleteById(productId);
    }
}
