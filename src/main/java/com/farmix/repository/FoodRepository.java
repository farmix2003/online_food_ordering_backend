package com.farmix.repository;

import com.farmix.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query("SELECT f FROM Food f WHERE f.foodName LIKE %:keyword% OR f.category.name LIKE %:keyword%")
    List<Food> searchFood(@Param("keyword") String keyword);

     List<Food> findByRestaurantId(Long restaurantId);
}
