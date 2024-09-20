package com.example.Artisan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class APIException extends RuntimeException{

    public APIException(){
    }

    public APIException(String message){
        super(message);
    }
}
