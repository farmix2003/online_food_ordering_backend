package com.farmix.repository;

import com.farmix.entity.OrderedFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedFoodRepository extends JpaRepository<OrderedFood, Long> {

}
