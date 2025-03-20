package com.farmix.controller;

import com.farmix.dto.RestaurantDto;
import com.farmix.entity.Restaurant;
import com.farmix.entity.User;
import com.farmix.service.RestaurantService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<Restaurant>> getAllRestaurants(@RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        return new ResponseEntity<>(restaurants, HttpStatus.OK) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id, @RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.getRestaurantById(id);

        return new ResponseEntity<>(restaurant, HttpStatus.OK) ;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurant(@RequestParam String key,
                                                             @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        List<Restaurant> restaurants = restaurantService.searchRestaurants(key);

        return new ResponseEntity<>(restaurants, HttpStatus.OK) ;
    }
    @PutMapping("/{id}/add-favourites")
    public ResponseEntity<RestaurantDto> addToFavourites(@PathVariable Long id,
                                                         @RequestHeader("Authorization") String jwt
                                                         ) throws Exception {
    User user = userService.findUserByJwtToken(jwt);
    RestaurantDto restaurantDto = restaurantService.addToFavourites(id, user);

    return new ResponseEntity<>(restaurantDto, HttpStatus.OK);
    }
}

