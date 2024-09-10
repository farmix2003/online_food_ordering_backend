package com.farmix.request;

import com.farmix.entity.Address;
import lombok.Data;

@Data
public class OrderRequest {

    private Long restaurantId;
    private Address shippingAddress;

}
