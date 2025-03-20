package com.farmix.service.serviceImpl;

import com.farmix.entity.*;
import com.farmix.exception.FoodNotFoundException;
import com.farmix.repository.ImageRepository;
import com.farmix.repository.MenuRepository;
import com.farmix.request.FoodRequest;
import com.farmix.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    @Transactional
    public Menu addFood(FoodRequest req, Category category, Restaurant restaurant) {

        List<Image> images = imageRepository.findAllById(req.getImageIds());

        if (req.getFoodName() == null || req.getFoodName().isEmpty()) {
            throw new IllegalArgumentException("Food name must not be null or empty.");
        }


        Menu food = new Menu();
        food.setCategory(category);
        food.setRestaurant(restaurant);
        food.setFoodName(req.getFoodName());
        food.setDescription(req.getDescription());
        food.setImagesList(images);
        food.setExtrasList(req.getExtras());
        food.setPrice(req.getPrice());
        Menu savedFood = menuRepository.save(food);

        images.forEach(image -> image.setMenu(savedFood));

        imageRepository.saveAll(images);

        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    @Override
    @Transactional
    public void deleteFood(Long id) throws Exception {
        Menu food = getFoodById(id);
        food.setRestaurant(null);
        menuRepository.save(food);
    }

    @Override
    public Menu getFoodById(Long id) throws Exception {
      return menuRepository.findById(id)
              .orElseThrow(() -> new FoodNotFoundException("Food not found with id "+id));
    }

    @Override
    @Transactional
    public List<Menu> getAllFoods(Long restaurantId, String category) throws Exception {
        List<Menu> menuList = menuRepository.findByRestaurantId(restaurantId);
        if(category != null && !category.isEmpty()){
            menuList = filterByFoodCategory(menuList, category);
        }
        return menuList;
    }

    private List<Menu> filterByFoodCategory(List<Menu> foods, String category) {
      return foods.stream().filter(food ->
              food.getCategory() != null && food.getCategory().getName().equals(category)
              ).collect(Collectors.toList());
    }

    @Override
    public List<Menu> searchFood(String keyword) throws Exception {
        return menuRepository.searchFood(keyword);
    }

    @Override
    @Transactional
    public Menu updateAvailableStatus(Long id) throws Exception {
        Menu food = getFoodById(id);
        food.setAvailable(!food.isAvailable());
        return menuRepository.save(food);
    }
}
