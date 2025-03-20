package com.farmix.dto;

import com.farmix.entity.Image;
import com.farmix.entity.Restaurant;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestaurantDto extends Restaurant {

    private Long id;
    private String title;
    private List<Image> images;
    private String description;
}
