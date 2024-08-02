package com.farmix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName;

    private String description;

    private Double price;

    @ManyToOne
    private Category category;

    @Column(length = 1000)
    @ElementCollection
    private List<String> imagesList;

    private boolean isAvailable;

    @ManyToOne
    private Restaurant restaurant;

    private boolean isSeasonal;

    @ManyToMany
    private List<Extras> extrasList = new ArrayList<>();

    private Date creationDate;
}
