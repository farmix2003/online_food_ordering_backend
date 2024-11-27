package com.farmix.exception;

public class FoodNotFoundException extends RuntimeException {
    public FoodNotFoundException(String message){
        super(message);
    }
}
