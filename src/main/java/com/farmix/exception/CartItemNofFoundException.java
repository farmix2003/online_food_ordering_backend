package com.farmix.exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class CartItemNofFoundException extends RuntimeException {

    public CartItemNofFoundException(String message){
        super(message);
    }

}
