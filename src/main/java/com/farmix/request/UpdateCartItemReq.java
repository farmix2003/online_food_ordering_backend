package com.farmix.request;

import lombok.Data;

@Data
public class UpdateCartItemReq {

    private Long id;
    private int quantity;
}
