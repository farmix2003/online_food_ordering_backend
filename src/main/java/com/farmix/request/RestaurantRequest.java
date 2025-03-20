package com.farmix.request;

import com.farmix.entity.Address;
import com.farmix.entity.ContactInfo;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    private List<MultipartFile> images;
}
