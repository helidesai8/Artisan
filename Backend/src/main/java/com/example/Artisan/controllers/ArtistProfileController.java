package com.example.Artisan.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.DTOs.ArtistStoryDTO;
import com.example.Artisan.exceptions.InvalidFileException;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.responses.ProfileImageApiResponse;
import com.example.Artisan.responses.StoryImageApiResponse;
import com.example.Artisan.services.artist.ArtistService;
import com.example.Artisan.services.profile.ArtistProfileImageService;
import com.example.Artisan.services.profile.ArtistProfileService;
import com.example.Artisan.services.story.ArtistStoryImageService;
import com.example.Artisan.services.story.ArtistStoryService;
import com.example.Artisan.utils.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import jakarta.validation.Valid;

/**
 * The controller class that handles the API endpoints related to artist profiles.
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1/artist/profile")
public class ArtistProfileController {

    @Autowired
    private ArtistProfileService artistProfileService;

    @Autowired
    private ArtistService artistService;


    @Autowired
    private ArtistProfileImageService profileImageService;

    private final TokenExtractor tokenExtractor;

    @Autowired
    private ArtistStoryService artistStoryService;

    @Autowired
    private ArtistStoryImageService artistStoryImageService;

    @Autowired
    public ArtistProfileController(ArtistProfileService artistProfileService,
                                   ArtistService artistService,
                                   ArtistProfileImageService profileImageService,
                                   TokenExtractor tokenExtractor,
                                   ArtistStoryService artistStoryService,
                                   ArtistStoryImageService artistStoryImageService) {
        this.artistProfileService = artistProfileService;
        this.artistService = artistService;
        this.profileImageService = profileImageService;
        this.tokenExtractor = tokenExtractor;
        this.artistStoryService = artistStoryService;
        this.artistStoryImageService = artistStoryImageService;
    }

    @PutMapping(" ")
    public ResponseEntity<?> updateArtist(@Valid @RequestBody ArtistDTO artistDto, BindingResult result) {
        // Extract the logged-in user's email from the security context
        String email = tokenExtractor.extractEmailFromToken();

        // Use ArtistService to get the artist based on email
        ArtistDTO existingArtist = this.artistService.getArtist(email);
        if (existingArtist == null) {
            throw new ResourceNotFoundException("Artist", "Email", email);
        }

        // Check for validation errors
        if (result.hasErrors()) {
            // Construct a validation error response
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ProfileImageApiResponse("Validation errors", false));
        }

        // Use ArtistProfileService to update the artist
        ArtistDTO updatedArtist = this.artistProfileService.update(artistDto, existingArtist.getArtistId());
        return ResponseEntity.ok(updatedArtist);
    }

    @GetMapping(" ")
    public ResponseEntity<ArtistDTO> getSingleArtist() {
        // Extract the logged-in artist's Id  from the security context
        String email = tokenExtractor.extractEmailFromToken();
        Long artistId = tokenExtractor.extractArtistIdFromToken();

        // Use ArtistService to get the artist based on email
        ArtistDTO existingArtist = this.artistProfileService.getArtistDetails(artistId);
        if (existingArtist == null) {
            throw new ResourceNotFoundException("Artist", "Email", email);
        }

        return ResponseEntity.ok(existingArtist);
    }

    @GetMapping("/details/{artistId}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long artistId) {
        if(artistId <= 0){
            return ResponseEntity.badRequest().body(null);
        }

        // Use ArtistService to get the artist based on id
        ArtistDTO existingArtist = this.artistProfileService.getArtistDetails(artistId);
        if (existingArtist == null) {
            throw new ResourceNotFoundException("Artist", "artistId", artistId);
        }
        existingArtist.setPassword(null);
        return ResponseEntity.ok(existingArtist);
    }



    @PostMapping("/profileImage")
    public ResponseEntity<ProfileImageApiResponse> uploadProfileImage(@RequestParam("image") MultipartFile image) {
        try {
            // Extract artist ID from the access token
            Long artistId = tokenExtractor.extractArtistIdFromToken();

            // Upload profile image
            String imageUrl = profileImageService.uploadProfileImage(artistId, image);


            // Return base64-encoded image string in the response
            return ResponseEntity.ok(new ProfileImageApiResponse("Profile image uploaded successfully.", true, imageUrl));
        } catch (IOException e) {
            var message = "Failed to upload profile image.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProfileImageApiResponse(message, false));
        }
    }


    @DeleteMapping("/profileImage")
    public ResponseEntity<ProfileImageApiResponse> deleteProfileImage() {
        try {
        Long artistId = tokenExtractor.extractArtistIdFromToken();
        profileImageService.deleteProfileImage(artistId);
        return ResponseEntity.ok(new ProfileImageApiResponse("Profile image deleted successfully.", true));
    } catch (ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProfileImageApiResponse(e.getMessage(), false));
    } catch (InvalidFileException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProfileImageApiResponse(e.getMessage(), false));
    }
    }


    @PutMapping("/story")
    public ResponseEntity<ArtistStoryDTO> addOrUpdateStory(@RequestBody ArtistStoryDTO storyDto) {
        Long artistId = tokenExtractor.extractArtistIdFromToken();
        ArtistStoryDTO savedStory = artistStoryService.addOrUpdateStory(artistId, storyDto);
        return ResponseEntity.ok(savedStory);
    }

    @PostMapping("/story/images")
    public ResponseEntity<StoryImageApiResponse> uploadStoryImages(@RequestParam("images") MultipartFile[] images) {
        Long artistId = tokenExtractor.extractArtistIdFromToken();
        Long storyId = artistStoryImageService.getStoryIdByArtistId(artistId);
        try {
            List<String> uploadedFileNames = artistStoryImageService.uploadStoryImage(storyId, images);
            return ResponseEntity.ok(new StoryImageApiResponse("Images uploaded for story successfully", true, uploadedFileNames));
        } catch (InvalidFileException e) {
            return ResponseEntity.badRequest().body(new StoryImageApiResponse(e.getMessage(), false));
        } catch (IOException e) {
            var errorResponse = new StoryImageApiResponse("Failed to upload images for story", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @DeleteMapping("/story/images")
    public ResponseEntity<StoryImageApiResponse> deleteStoryImages() {
        Long artistId = tokenExtractor.extractArtistIdFromToken();
        Long storyId = artistStoryImageService.getStoryIdByArtistId(artistId);
        try {
            artistStoryImageService.deleteStoryImages(storyId);
            return ResponseEntity.ok(new StoryImageApiResponse("Images deleted from story successfully.", true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StoryImageApiResponse(e.getMessage(), false));
        }
    }

}


