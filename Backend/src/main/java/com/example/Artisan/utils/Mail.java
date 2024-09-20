package com.example.Artisan.utils;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Mail {
    String email;
    String subject;
    String content;
}
