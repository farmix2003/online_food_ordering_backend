package com.farmix;


import com.farmix.controller.RestaurantController;
import com.farmix.entity.Restaurant;
import com.farmix.entity.User;
import com.farmix.service.RestaurantService;
import com.farmix.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp(){
        user = new User();
        user.setId(1L);
    }

    @Test
    public void testGetAllRestaurants() throws Exception{

        Restaurant r1 = new Restaurant();
        r1.setId(1L);
        r1.setName("Restaurant One");

        Restaurant r2 = new Restaurant();
        r2.setId(1L);
        r2.setName("Restaurant Two");

        List<Restaurant> restaurants = Arrays.asList(r1, r2);

        when(userService.findUserByJwtToken("Bearer token")).thenReturn(user);
        when(restaurantService.getAllRestaurants()).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Restaurant One")))
                .andExpect(jsonPath("$[0].name", Matchers.is("Restaurant Two")));
    }
}
