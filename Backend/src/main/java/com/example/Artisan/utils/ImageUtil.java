/**
    * Checks if the given image file has a valid image type.
    * 
    * @param image the image file to be checked
    * @return true if the image file has a valid image type, false otherwise
    * @throws IOException if an I/O error occurs while reading the image file
    */
package com.example.Artisan.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png");

    public static boolean isValidImageType(MultipartFile image) throws IOException {
        return ALLOWED_IMAGE_TYPES.contains(image.getContentType());
    }
}
