package com.farmix.request;

import jakarta.annotation.Nonnull;
import lombok.Data;

@Data
public class LoginRequest {

     private String email;

     private String password;
}
