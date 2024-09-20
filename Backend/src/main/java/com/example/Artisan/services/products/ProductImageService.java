package com.example.Artisan.services.products;
import com.example.Artisan.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductImageService {

    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductImageService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    public List<String> uploadImages(List<MultipartFile> images) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile image : images) {
            Map uploadResult = cloudinaryService.uploadImage(image);
            urls.add((String) uploadResult.get("url"));
        }
        return urls;
    }
}

