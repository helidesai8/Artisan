package com.example.Artisan.responses;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryImageApiResponse {

    private String message;
    private boolean success;
    private List<String> storyImageNames;

    // Overloaded constructor with two parameters
    public StoryImageApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    // Overloaded constructor with three parameters
    public StoryImageApiResponse(String message, boolean success,List<String> storyImageNames) {
        this.message = message;
        this.success = success;
        this.storyImageNames=storyImageNames;
    }

}
