package com.example.Artisan.config;

import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CloudinaryConfigTest {

    private CloudinaryConfig cloudinaryConfig;

    @BeforeEach
    public void setup() {
        cloudinaryConfig = new CloudinaryConfig();
        ReflectionTestUtils.setField(cloudinaryConfig, "cloudName", "testCloud");
        ReflectionTestUtils.setField(cloudinaryConfig, "apiKey", "testKey");
        ReflectionTestUtils.setField(cloudinaryConfig, "apiSecret", "testSecret");
    }

    @Test
    void cloudinary_returnsCloudinaryInstance() {
        Cloudinary cloudinary = cloudinaryConfig.cloudinary();

        assertNotNull(cloudinary);
        assertEquals("testCloud", cloudinary.config.cloudName);
    }
}