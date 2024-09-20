package com.example.Artisan.services.products;

import com.example.Artisan.services.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ProductImageServiceTest {

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ProductImageService productImageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void uploadImages_singleImage_success() throws IOException {
        MultipartFile image = new MockMultipartFile("image", "hello.png", "image/png", "some image".getBytes());
        Map uploadResult = new HashMap<>();
        uploadResult.put("url", "http://example.com/image1.png");
        when(cloudinaryService.uploadImage(image)).thenReturn(uploadResult);
        assertEquals(Collections.singletonList("http://example.com/image1.png"), productImageService.uploadImages(Collections.singletonList(image)));
    }

    @Test
    public void uploadImages_multipleImages_success() throws IOException {
        MultipartFile image1 = new MockMultipartFile("image1", "hello1.png", "image/png", "some image".getBytes());
        MultipartFile image2 = new MockMultipartFile("image2", "hello2.png", "image/png", "some image".getBytes());
        Map<String, String> uploadResult1 = new HashMap<>();
        uploadResult1.put("url", "http://example.com/image1.png");
        Map<String, String> uploadResult2 = new HashMap<>();
        uploadResult2.put("url", "http://example.com/image2.png");
        when(cloudinaryService.uploadImage(image1)).thenReturn(uploadResult1);
        when(cloudinaryService.uploadImage(image2)).thenReturn(uploadResult2);
        assertEquals(Arrays.asList("http://example.com/image1.png", "http://example.com/image2.png"), productImageService.uploadImages(Arrays.asList(image1, image2)));
    }

    @Test
    public void uploadImages_noImages_success() throws IOException {
        assertEquals(Collections.emptyList(), productImageService.uploadImages(Collections.emptyList()));
    }
}