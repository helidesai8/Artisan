package com.example.Artisan.services;

import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class CloudinaryServiceTest {

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private com.cloudinary.Uploader uploader;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void uploadImageReturnsMapWhenSuccessful() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
        Map<String, String> expected = new HashMap<>();
        expected.put("url", "http://test.com");

        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(expected);

        Map<String, String> actual = cloudinaryService.uploadImage(file);

        assertEquals(expected, actual);
    }

    @Test
    void deleteImageDeletesImageWhenSuccessful() throws IOException {
        when(uploader.destroy(anyString(), anyMap())).thenReturn(new HashMap<>());

        cloudinaryService.deleteImage("http://test.com/test.jpg");
    }

    @Test
    void getImagePublicIdReturnsPublicIdWhenSuccessful() {
        String imageUrl = "http://test.com/test.jpg";
        String expected = "test";

        String actual = cloudinaryService.getImagePublicId(imageUrl);

        assertEquals(expected, actual);
    }
}