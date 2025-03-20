//package com.farmix.repository;
//
//import com.farmix.entity.Restaurant;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
//
//    @Query("SELECT r FROM Restaurant r WHERE lower(r.name) LIKE lower(concat('%',:query, '%') ) "+
//    "OR lower(r.cuisineType) LIKE lower(concat('%', :query, '%') ) ")
//    public List<Restaurant> searchRestaurants(String query);
//
//    public Restaurant findByOwnerId(Long id);
//}
package com.farmix.repository;

import com.farmix.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(r.cuisineType) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Restaurant> searchRestaurants(String query);

    Restaurant findByOwnerId(Long id);
}