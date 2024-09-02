package com.farmix.service.serviceImpl;

import com.farmix.entity.Category;
import com.farmix.entity.Food;
import com.farmix.entity.Restaurant;
import com.farmix.repository.FoodRepository;
import com.farmix.request.FoodRequest;
import com.farmix.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public Food addFood(FoodRequest req, Category category, Restaurant restaurant) {
        Food food = new Food();
        food.setCategory(category);
        food.setRestaurant(restaurant);
        food.setFoodName(req.getFoodName());
        food.setDescription(req.getDescription());
        food.setImagesList(req.getImages());
        food.setExtrasList(req.getExtras());
        food.setPrice(req.getPrice());
        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    @Override
    public void deleteFood(Long id) throws Exception {
        Food food = getFoodById(id);
        food.setRestaurant(null);
        foodRepository.save(food);
    }

    @Override
    public Food getFoodById(Long id) throws Exception {
        Optional<Food> optionalFood = foodRepository.findById(id);
        if (optionalFood.isEmpty()) {
            throw new Exception("Food is not exist.");
        }
        return optionalFood.get();
    }

    @Override
    public List<Food> getAllFoods(Long restaurantId, String category) throws Exception {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
        if(category != null && !category.isEmpty()){
         foods = filterByFoodCategory(foods, category);
        }
        return foods;
    }

    private List<Food> filterByFoodCategory(List<Food> foods, String category) {
      return foods.stream().filter(food -> {
          if(food.getCategory() != null){
              return food.getCategory().getName().equals(category);
          };
          return false;
      }).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) throws Exception {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food updateAvailableStatus(Long id) throws Exception {
        Food food = getFoodById(id);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
