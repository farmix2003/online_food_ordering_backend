package com.farmix.controller;

import com.farmix.dto.CategoryDTO;
import com.farmix.entity.Category;
import com.farmix.entity.User;
import com.farmix.service.CategoryService;
import com.farmix.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserService userService;

    @Test
    void whenGetRestaurantCategories_thenReturnJsonArray() throws Exception {
        User user = new User();
        user.setId(1L);

        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setId(1L);
        categoryDto.setName("Vegetarian");

        when(userService.findUserByJwtToken(anyString())).thenReturn(user);
        when(categoryService.findCategoriesByRestaurantId(user.getId())).thenReturn(Collections.singletonList(categoryDto));

        mvc.perform(MockMvcRequestBuilders.get("/api/category")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(categoryDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(categoryDto.getName()));
    }
}