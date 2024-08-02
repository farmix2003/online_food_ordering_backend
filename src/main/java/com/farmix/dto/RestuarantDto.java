package com.farmix.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class RestuarantDto {

    @Id
    private Long id;
    private String title;

    @Column(length = 1000)
    private List<String> images;
    private String description;
}
