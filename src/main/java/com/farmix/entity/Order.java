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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User customer;

    @ManyToOne
    private Restaurant restaurant;

    private String orderStatus;

    private Long totalAmount;

    private Date createdAt;

    @ManyToOne
    private Address shippingAddress;

    @OneToMany
    private List<OrderedFood> orderedFoodList;

    private int totalOfOrder;

    private double totalPrice;
}
