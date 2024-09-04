package com.farmix.repository;

import com.farmix.entity.Extras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtrasRepository extends JpaRepository<Extras, Long> {

    public List<Extras> findExtrasByRestaurantId(Long restaurantId);

}
