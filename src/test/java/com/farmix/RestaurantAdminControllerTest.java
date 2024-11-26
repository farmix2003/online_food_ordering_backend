package com.farmix;

import com.farmix.controller.RestaurantAdminController;
import com.farmix.entity.Restaurant;
import com.farmix.entity.User;
import com.farmix.request.CreateRestaurantRequest;
import com.farmix.response.MessageResponse;
import com.farmix.service.RestaurantService;
import com.farmix.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantAdminController.class)
public class RestaurantAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private UserService userService;

    private User mockUser;
    private Restaurant mockRestaurant;

    @BeforeEach
    public void setup() {
        // Create mock User
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("admin@example.com");

        // Create mock Restaurant
        mockRestaurant = new Restaurant();
        mockRestaurant.setId(1L);
        mockRestaurant.setName("Mock Restaurant");
    }

    @Test
    public void testCreateRestaurant() throws Exception {
        CreateRestaurantRequest restaurantRequest = new CreateRestaurantRequest();
        restaurantRequest.setName("Test Restaurant");

        // Mock the services
        Mockito.when(userService.findUserByJwtToken(any(String.class))).thenReturn(mockUser);
        Mockito.when(restaurantService.createRestaurant(any(CreateRestaurantRequest.class), any(User.class)))
                .thenReturn(mockRestaurant);

        String requestBody = """
                {
                "name": "Test Restaurant"
                }
                """;

        mockMvc.perform(post("/api/admin/restaurants/add")
                        .header("Authorization", "Bearer mock-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mock Restaurant"));
    }

    @Test
    public void testUpdateRestaurant() throws Exception {
        CreateRestaurantRequest req = new CreateRestaurantRequest();
        req.setName("Updated Restaurant");

        Mockito.when(userService.findUserByJwtToken(any(String.class))).thenReturn(mockUser);

        String requestBody = """
                {
                "name": "Updated Restaurant"
                }
                """;

        mockMvc.perform(put("/api/admin/restaurants/update/{id}", 1L)
                        .header("Authorization", "Bearer mock-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Mockito.verify(restaurantService).updateRestaurant(eq(1L), any(CreateRestaurantRequest.class));
    }

    @Test
    public void testDeleteRestaurant() throws Exception {
        Mockito.when(userService.findUserByJwtToken(any(String.class))).thenReturn(mockUser);

        mockMvc.perform(delete("/api/admin/restaurants/delete/{id}", 1L)
                        .header("Authorization", "Bearer mock-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Restaurant deleted"));

        Mockito.verify(restaurantService).deleteRestaurant(1L);
    }

    @Test
    public void testUpdateRestaurantStatus() throws Exception {
        Mockito.when(userService.findUserByJwtToken(any(String.class))).thenReturn(mockUser);
        Mockito.when(restaurantService.updateRestaurantStatus(1L)).thenReturn(mockRestaurant);

        mockMvc.perform(put("/api/admin/restaurants/{id}/status", 1L)
                        .header("Authorization", "Bearer mock-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mock Restaurant"));

        Mockito.verify(restaurantService).updateRestaurantStatus(1L);
    }

    @Test
    public void testFindRestaurantByUserId() throws Exception {
        Mockito.when(userService.findUserByJwtToken(any(String.class))).thenReturn(mockUser);
        Mockito.when(restaurantService.getRestaurantByUserId(mockUser.getId())).thenReturn(mockRestaurant);

        mockMvc.perform(get("/api/admin/restaurants/user")
                        .header("Authorization", "Bearer mock-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mock Restaurant"));

        Mockito.verify(restaurantService).getRestaurantByUserId(mockUser.getId());
    }
}
