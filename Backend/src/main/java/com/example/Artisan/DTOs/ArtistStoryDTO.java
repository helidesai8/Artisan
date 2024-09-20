package com.example.Artisan.DTOs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistStoryDTO {
    private String story;
    private LocalDate datePosted;
    private List<String> storyImageUrls = new ArrayList<>();

    public void setStoryId(long l) {
    }

    public void setContent(String storyContent) {
    }
}
