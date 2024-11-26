package com.farmix.service.serviceImpl;

import com.farmix.entity.Category;
import com.farmix.entity.Menu;
import com.farmix.entity.Restaurant;
import com.farmix.repository.FoodRepository;
import com.farmix.request.FoodRequest;
import com.farmix.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public Menu addFood(FoodRequest req, Category category, Restaurant restaurant) {
        Menu food = new Menu();
        food.setCategory(category);
        food.setRestaurant(restaurant);
        food.setFoodName(req.getFoodName());
        food.setDescription(req.getDescription());
        food.setImagesList(req.getImages());
        food.setExtrasList(req.getExtras());
        food.setPrice(req.getPrice());
        Menu savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    @Override
    public void deleteFood(Long id) throws Exception {
        Menu food = getFoodById(id);
        food.setRestaurant(null);
        foodRepository.save(food);
    }

    @Override
    public Menu getFoodById(Long id) throws Exception {
        Optional<Menu> optionalFood = foodRepository.findById(id);
        if (optionalFood.isEmpty()) {
            throw new Exception("Food is not exist.");
        }
        return optionalFood.get();
    }

    @Override
    public List<Menu> getAllFoods(Long restaurantId, String category) throws Exception {
        List<Menu> foods = foodRepository.findByRestaurantId(restaurantId);
        if(category != null && !category.isEmpty()){
         foods = filterByFoodCategory(foods, category);
        }
        return foods;
    }

    private List<Menu> filterByFoodCategory(List<Menu> foods, String category) {
      return foods.stream().filter(food -> {
          if(food.getCategory() != null){
              return food.getCategory().getName().equals(category);
          };
          return false;
      }).collect(Collectors.toList());
    }

    @Override
    public List<Menu> searchFood(String keyword) throws Exception {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Menu updateAvailableStatus(Long id) throws Exception {
        Menu food = getFoodById(id);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
