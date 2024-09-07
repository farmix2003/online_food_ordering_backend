package com.farmix.request;

import lombok.Data;

import java.util.List;

@Data
public class CartItemReq {

    private Long foodId;
    private int quantity;

    private List<String> extras;
}
