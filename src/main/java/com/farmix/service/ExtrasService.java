package com.farmix.service;


import com.farmix.entity.Extras;

import java.util.List;
import java.util.Optional;

public interface ExtrasService {

    Extras addExtras(String extras, Long restaurantId) throws Exception;

    List<Extras> findExtrasByRestaurantId(Long restaurantId) throws Exception;

    void deleteExtras(Long extrasId) throws Exception;

    Optional<Extras> findExtrasById(Long extrasId) throws Exception;

    Extras updateInStock(Long id);
}
