package com.farmix.controller;

import com.farmix.dto.CategoryDTO;
import com.farmix.entity.Category;
import com.farmix.entity.User;
import com.farmix.service.CategoryService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @PostMapping("/admin/category/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category,
                                                @RequestHeader("Authorization") String jwt
                                                ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Category newCategory = categoryService.createCategory(category.getName(), user.getId());
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDTO>> getRestaurantCategories(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<CategoryDTO> categories = categoryService.findCategoriesByRestaurantId(user.getId());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id,
            @RequestHeader("Authorization") String jwt) throws Exception {
      User user = userService.findUserByJwtToken(jwt);
      Category category = categoryService.findCategoryById(id);
      return new ResponseEntity<>(category, HttpStatus.OK);

    }


}
