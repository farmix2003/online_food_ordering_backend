package com.farmix.controller;

import com.farmix.entity.Restaurant;
import com.farmix.entity.User;
import com.farmix.request.RestaurantRequest;
import com.farmix.response.MessageResponse;
import com.farmix.service.RestaurantService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
public class RestaurantAdminController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Restaurant> createRestaurant(@ModelAttribute RestaurantRequest restaurant,
                                                       @RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Restaurant newRestaurant = restaurantService.createRestaurant(restaurant, user);

        return new ResponseEntity<>(newRestaurant, HttpStatus.CREATED) ;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
                                                       @RequestHeader("Authorization") String jwt,
                                                       @ModelAttribute RestaurantRequest req) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        restaurantService.updateRestaurant(id, req);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(@PathVariable Long id,
                                                            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        restaurantService.deleteRestaurant(id);
        MessageResponse msg = new MessageResponse();
        msg.setMessage("Restaurant deleted");
             return new ResponseEntity<>(msg, HttpStatus.OK);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(@PathVariable Long id,
                                                            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant res = restaurantService.updateRestaurantStatus(id);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<Restaurant> findRestaurantByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant res = restaurantService.getRestaurantByUserId(user.getId());

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
