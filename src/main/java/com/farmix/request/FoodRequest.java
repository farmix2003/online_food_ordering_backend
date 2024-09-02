package com.farmix.request;

import com.farmix.entity.Category;
import com.farmix.entity.Extras;
import lombok.Data;

import java.util.List;

@Data
public class FoodRequest {
    private String foodName;
    private String description;
    private Double price;

    private Category category;
    private List<String> images;

    private Long restaurantId;
    private List<Extras> extras;
}
