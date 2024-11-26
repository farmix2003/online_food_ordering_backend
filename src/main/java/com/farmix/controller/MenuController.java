package com.farmix.controller;

import com.farmix.entity.Menu;
import com.farmix.entity.User;
import com.farmix.service.MenuService;
import com.farmix.service.RestaurantService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class MenuController {

    @Autowired
    UserService userService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    MenuService foodService;

    @GetMapping("/search")
    public ResponseEntity<List<Menu>> searchFood(@RequestParam String name,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
       List<Menu> foods = foodService.searchFood(name);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<Menu>> getRestaurantFoods(@PathVariable Long id,
                                                         @RequestParam(required = false) String category,
                                                         @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Menu> foods = foodService.getAllFoods(id, category);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
}
