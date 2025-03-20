//package com.farmix.repository;
//
//import com.farmix.entity.Menu;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface MenuRepository extends JpaRepository<Menu, Long> {
//
//    @Query("SELECT f FROM Menu f WHERE f.foodName LIKE %:keyword% OR f.category.name LIKE %:keyword%")
//    List<Menu> searchFood(@Param("keyword") String keyword);
//
//     List<Menu> findByRestaurantId(Long restaurantId);
//}
package com.farmix.repository;

import com.farmix.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT f FROM Menu f WHERE f.foodName LIKE CONCAT('%', :keyword, '%') " +
            "OR f.category.name LIKE CONCAT('%', :keyword, '%')")
    List<Menu> searchFood(@Param("keyword") String keyword);

    List<Menu> findByRestaurantId(Long restaurantId);
}