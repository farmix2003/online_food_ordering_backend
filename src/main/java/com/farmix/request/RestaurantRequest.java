package com.farmix.request;

import com.farmix.entity.Address;
import com.farmix.entity.ContactInfo;
import com.farmix.entity.Image;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantRequest {

    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInfo contactInfo;
    private String openingHours;
    private String closingHours;
    private List<Long> imageIds;
}
