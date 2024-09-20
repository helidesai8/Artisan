package com.example.Artisan.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

/**
 * This class provides services for uploading and deleting images from Cloudinary.
 */
@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Uploads an image file to Cloudinary.
     *
     * @param file The image file to be uploaded.
     * @return A map containing the details of the uploaded image.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public Map<String, String> uploadImage(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
    }

    /**
     * Deletes an image from Cloudinary.
     *
     * @param imageUrl The URL of the image to be deleted.
     */
    public void deleteImage(String imageUrl) {
        String publicId = getImagePublicId(imageUrl);
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            // Handle Cloudinary image deletion failure
            throw new RuntimeException("Failed to delete image from Cloudinary: " + imageUrl, e);
        }
    }

    /**
     * Extracts the public ID of an image from its URL.
     *
     * @param imageUrl The URL of the image.
     * @return The public ID of the image.
     */
    String getImagePublicId(String imageUrl) {
        int lastSlashIndex = imageUrl.lastIndexOf('/');
        int extensionIndex = imageUrl.lastIndexOf('.');
        return imageUrl.substring(lastSlashIndex + 1, extensionIndex);
    }
}

