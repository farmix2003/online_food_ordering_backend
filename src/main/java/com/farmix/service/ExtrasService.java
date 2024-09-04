package com.farmix.service;


import com.farmix.entity.Extras;

import java.util.List;
import java.util.Optional;

public interface ExtrasService {

    public Extras addExtras(String extras, Long restaurantId) throws Exception;

    public List<Extras> findExtrasByRestaurantId(Long restaurantId) throws Exception;

    public void deleteExtras(Long extrasId) throws Exception;

    public Optional<Extras> findExtrasById(Long extrasId) throws Exception;

    public Extras updateInStock(Long id);
}
