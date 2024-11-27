package com.farmix.service.serviceImpl;

import com.farmix.entity.Extras;
import com.farmix.entity.Restaurant;
import com.farmix.repository.ExtrasRepository;
import com.farmix.service.ExtrasService;
import com.farmix.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExtrasServiceImpl implements ExtrasService {

    @Autowired
    private ExtrasRepository extrasRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public Extras addExtras(String name, Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        Extras newExtras = new Extras();

        newExtras.setName(name);
        newExtras.setRestaurant(restaurant);

        return extrasRepository.save(newExtras);
    }

    @Override
    public List<Extras> findExtrasByRestaurantId(Long restaurantId) throws Exception {

        return extrasRepository.findExtrasByRestaurantId(restaurantId);
    }

    @Override
    public void deleteExtras(Long extrasId) throws Exception {
        extrasRepository.deleteById(extrasId);
    }

    @Override
    public Optional<Extras> findExtrasById(Long extrasId) throws Exception {
        return extrasRepository.findById(extrasId);
    }

    @Override
    public Extras updateInStock(Long id) {

        Extras extras = extrasRepository.findById(id).get();
        extras.setInStock(!extras.isInStock());
        return null;
    }
}
