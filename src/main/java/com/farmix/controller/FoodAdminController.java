package com.farmix.controller;

import com.farmix.entity.Food;
import com.farmix.entity.Restaurant;
import com.farmix.entity.User;
import com.farmix.request.FoodRequest;
import com.farmix.response.MessageResponse;
import com.farmix.service.FoodService;
import com.farmix.service.RestaurantService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class FoodAdminController {

    @Autowired
    UserService userService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    FoodService foodService;

    @PostMapping("/add-food")
    public ResponseEntity<Food> addFood(@RequestBody FoodRequest food,
                                        @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantById(food.getRestaurantId());
        Food newFood = foodService.addFood(food, food.getCategory(), restaurant);
        return new ResponseEntity<>(newFood, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwt
                                           ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        foodService.deleteFood(id);

        MessageResponse response = new MessageResponse();
        response.setMessage("Food deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Food> updateFoodAvailabilityStatus(@PathVariable Long id,
                                                             @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Food food = foodService.updateAvailableStatus(id);

        return new ResponseEntity<>(food, HttpStatus.OK);
    }
}
