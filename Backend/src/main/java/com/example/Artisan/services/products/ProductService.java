package com.example.Artisan.services.products;

import com.example.Artisan.DTOs.CheckoutDetailDTO;
import com.example.Artisan.DTOs.products.CheckoutProductDTO;
import com.example.Artisan.DTOs.products.ProductBaseDTO;
import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.products.ProductImage;
import com.example.Artisan.entities.products.ProductCategory;
import com.example.Artisan.entities.products.ProductInventory;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.exceptions.ArtistNotFoundException;
import com.example.Artisan.exceptions.ProductCategoryNotFoundException;
import com.example.Artisan.exceptions.ProductNotFoundException;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.repositories.products.ProductCategoryRepository;
import com.example.Artisan.repositories.products.ProductRepository;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ArtistRepository artistRepository;
    private final ModelMapper modelMapper;
    private final ProductInventoryService _productInventoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, ArtistRepository artistRepository,
                          ProductCategoryRepository productCategoryRepository, ModelMapper modelMapper,
                          ProductInventoryService productInventoryService) {
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.modelMapper = modelMapper;
        _productInventoryService = productInventoryService;
    }


    public ProductDTO saveProduct(ProductDTO productDTO, Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException(artistId));
        Product product = convertToEntity(productDTO, artist);
        Product savedProduct = productRepository.save(product);
        if (savedProduct != null) {
            return mapProductWithImagesAndCategories(savedProduct);
        } else {
            throw new APIException("Saved product is null");
        }
    }

    public ProductDTO mapProductWithImagesAndCategories(@NotNull Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setImageUrls(product.getImageUrls().stream().map(ProductImage::getUrl).collect(Collectors.toList()));
        productDTO.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        productDTO.setIsActive(product.getIsActive());
        productDTO.setArtistId(product.getArtist().getArtistId());
        if (product.getProductInventory() != null) {
            productDTO.setQuantity(product.getProductInventory().getQuantity());
        } else {
            productDTO.setQuantity(0);
        }

        return productDTO;
    }

    private Product convertToEntity(ProductDTO productDTO, Artist artist) {
        try {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setArtist(artist);

            if (productDTO.getCategoryName() != null) {
                ProductCategory category = productCategoryRepository.findByName(productDTO.getCategoryName());
                product.setCategory(category);
            }


            if (productDTO.getImageUrls() != null && !productDTO.getImageUrls().isEmpty()) {
                Set<ProductImage> images = productDTO.getImageUrls().stream().map(url -> {
                    ProductImage image = new ProductImage();
                    image.setUrl(url);
                    image.setProduct(product);
                    return image;
                }).collect(Collectors.toSet());
                product.setImageUrls(images);
            }

        var productInventory = new ProductInventory();
        productInventory.setQuantity(productDTO.getQuantity());
        productInventory.setProduct(product);
        productInventory.setArtist(product.getArtist());
        product.setProductInventory(productInventory);

            return product;
        } catch (ProductCategoryNotFoundException e) {
            throw new APIException("Product category not found");
        } catch (ArtistNotFoundException e) {
            throw new APIException("Artist not found");
        } catch (Exception e) {
            throw new RuntimeException("Internal Server Error");
        }
    }

    public List<ProductDTO> getProductsByArtistId(Long artistId) {
        List<Product> products = productRepository.findByArtist_ArtistId(artistId);
        return products.stream().map(this::mapProductWithImagesAndCategories).collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO, List<String> imageUrls) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());

        if (existingProduct.getProductInventory() != null) {
            existingProduct.getProductInventory().setQuantity(productDTO.getQuantity());
        } else {
            System.out.println("Product inventory is null for product ID: " + id);
            ProductInventory newInventory = new ProductInventory();
            newInventory.setQuantity(productDTO.getQuantity());
            newInventory.setProduct(existingProduct);
            existingProduct.setProductInventory(newInventory);
        }

        // Update product images only if image URLs are provided and not empty
        if (imageUrls != null && !imageUrls.isEmpty()) {
            updateProductImages(existingProduct, imageUrls);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return mapProductWithImagesAndCategories(updatedProduct);
    }

    public void updateProductImages(Product product, List<String> imageUrls) {
        Set<ProductImage> currentImages = product.getImageUrls();
        Set<String> currentImageUrls = currentImages.stream().map(ProductImage::getUrl).collect(Collectors.toSet());

        List<String> newImageUrls = imageUrls.stream()
                .filter(url -> !currentImageUrls.contains(url))
                .toList();
        for (String url : newImageUrls) {
            ProductImage image = new ProductImage();
            image.setUrl(url);
            image.setProduct(product);
            product.getImageUrls().add(image);
        }
        List<ProductImage> imagesToRemove = currentImages.stream()
                .filter(image -> !imageUrls.contains(image.getUrl()))
                .toList();
        product.getImageUrls().removeAll(imagesToRemove);

    }

    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        existingProduct.setIsActive(!(existingProduct.getIsActive()));
        productRepository.save(existingProduct);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public CheckoutDetailDTO CheckoutProducts(List<ProductBaseDTO> products){
        if(products == null || products.isEmpty()){
            throw new IllegalArgumentException("No products in checkout");
        }
        try{
            CheckoutDetailDTO checkoutDetail = new CheckoutDetailDTO();
            List<CheckoutProductDTO> checkoutProducts = new ArrayList<CheckoutProductDTO>();
            for(var checkoutItem : products){
                var checkoutItemId =checkoutItem.getId();
                 var product = this.productRepository.findById(checkoutItemId)
                        .orElseThrow(() -> new ProductNotFoundException(checkoutItemId));

                var productInventory = this._productInventoryService.getInventoryByProductId(product.getId());
                // if order quantity is more than present inventory
                if(productInventory < checkoutItem.getQuantity()){
                    throw new APIException("Quantity of product " + product.getName() + " not available");
                }
                var checkoutProduct = modelMapper.map(product, CheckoutProductDTO.class);
                checkoutProduct.setQuantity(checkoutItem.getQuantity());

                // add image for product
                if(!product.getImageUrls().isEmpty()) {
                    var productImageStream = product.getImageUrls().stream();
                    var img = productImageStream.min(Comparator.comparing(ProductImage::getId)).get();
                    var imgUrl = img.getUrl();
                    checkoutProduct.setImg_url(imgUrl);
                }   
                checkoutProducts.add(checkoutProduct);
            }

            checkoutDetail.setProducts(checkoutProducts);
            var checkoutProductStream = checkoutProducts.stream();
            var itemValues = checkoutProductStream.map(x -> x.getPrice().multiply(BigDecimal.valueOf(x.getQuantity())));
            BigDecimal totalAmount = itemValues.reduce(BigDecimal.ZERO, BigDecimal::add);

            checkoutDetail.setTotalAmount(totalAmount);
            return checkoutDetail;
        }catch (Exception e){
            throw e;
        }
    }

}
