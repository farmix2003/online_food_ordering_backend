package com.farmix.service;

import com.farmix.dto.RestuarantDto;
import com.farmix.entity.Restaurant;
import com.farmix.entity.User;
import com.farmix.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {

    public Restaurant createRestaurant(CreateRestaurantRequest restaurant, User user) throws Exception;
    public Restaurant updateRestaurant(Long id, CreateRestaurantRequest restaurant) throws Exception;
    public Restaurant getRestaurantById(Long id) throws Exception;
    public List<Restaurant> getAllRestaurants() throws Exception;
    public void deleteRestaurant(Long id) throws Exception;
    public List<Restaurant> searchRestaurants(String keyword) throws Exception;
    public Restaurant getRestaurantByUserId(Long id) throws Exception;
    public RestuarantDto addToFavourites(Long id, User user) throws Exception;
    public Restaurant updateRestaurantStatus(Long id) throws Exception;
}
