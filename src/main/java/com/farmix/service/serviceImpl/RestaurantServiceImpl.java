package com.farmix.service.serviceImpl;

import com.farmix.dto.RestuarantDto;
import com.farmix.entity.Address;
import com.farmix.entity.Restaurant;
import com.farmix.entity.User;
import com.farmix.exception.RestaurantNotFoundException;
import com.farmix.repository.AddressRepository;
import com.farmix.repository.RestaurantRepository;
import com.farmix.repository.UserRepository;
import com.farmix.request.RestaurantRequest;
import com.farmix.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Restaurant createRestaurant(RestaurantRequest restaurant, User user) throws Exception {

        Address address = addressRepository.save(restaurant.getAddress());

        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName(restaurant.getName());
        newRestaurant.setAddress(address);
        newRestaurant.setDescription(restaurant.getDescription());
        newRestaurant.setContactInfo(restaurant.getContactInfo());
        newRestaurant.setOpeningHours(restaurant.getOpeningHours());
        newRestaurant.setClosingHours(restaurant.getClosingHours());
        newRestaurant.setCuisineType(restaurant.getCuisineType());
        newRestaurant.setImages(restaurant.getImages());
        newRestaurant.setRegistrationDate(LocalDateTime.now());
        newRestaurant.setOwner(user);

        return restaurantRepository.save(newRestaurant);
    }

    @Override
    public void updateRestaurant(Long id, RestaurantRequest restaurant) throws Exception {
        Restaurant updatedRestaurant = restaurantRepository.getReferenceById(id);

//        if (restaurant.getName() != null){
//            updatedRestaurant.setName(restaurant.getName());
//        }
//        if (restaurant.getAddress() != null) {
//            updatedRestaurant.setAddress(restaurant.getAddress());
//            addressRepository.save(restaurant.getAddress());
//        }
//        if (restaurant.getDescription() != null) {
//            updatedRestaurant.setDescription(restaurant.getDescription());
//        }
//        if (restaurant.getContactInfo() != null) {
//            updatedRestaurant.setContactInfo(restaurant.getContactInfo());
//        }
//        if (restaurant.getOpeningHours() != null) {
//            updatedRestaurant.setOpeningHours(restaurant.getOpeningHours());
//        }
//        if (restaurant.getClosingHours() != null) {
//            updatedRestaurant.setClosingHours(restaurant.getClosingHours());
//        }
//        if (restaurant.getCuisineType() != null) {
//            updatedRestaurant.setCuisineType(restaurant.getCuisineType());
//        }
//        if (restaurant.getImages() != null) {
//            updatedRestaurant.setImages(restaurant.getImages());
//        }

        Optional.ofNullable(restaurant.getName()).ifPresent(updatedRestaurant::setName);
        Optional.ofNullable(restaurant.getAddress()).ifPresent(updatedRestaurant::setAddress);
        Optional.ofNullable(restaurant.getDescription()).ifPresent(updatedRestaurant::setDescription);
        Optional.ofNullable(restaurant.getContactInfo()).ifPresent(updatedRestaurant::setContactInfo);
        Optional.ofNullable(restaurant.getOpeningHours()).ifPresent(updatedRestaurant::setOpeningHours);
        Optional.ofNullable(restaurant.getClosingHours()).ifPresent(updatedRestaurant::setClosingHours);
        Optional.ofNullable(restaurant.getCuisineType()).ifPresent(updatedRestaurant::setCuisineType);
        Optional.ofNullable(restaurant.getImages()).ifPresent(updatedRestaurant::setImages);

        restaurantRepository.save(updatedRestaurant);
    }

    @Override
    public Restaurant getRestaurantById(Long id) throws Exception {
       return restaurantRepository.findById(id)
               .orElseThrow(()-> new RestaurantNotFoundException("Restaurant not found with id "+id));

    }

    @Override
    public List<Restaurant> getAllRestaurants() throws Exception {
       return restaurantRepository.findAll();
    }

    @Override
    public void deleteRestaurant(Long id) throws Exception {
          Restaurant restaurant = getRestaurantById(id);
          restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> searchRestaurants(String keyword) throws Exception {
        return restaurantRepository.searchRestaurants(keyword);
    }

    @Override
    public Restaurant getRestaurantByUserId(Long id) throws Exception {
        return restaurantRepository.findByOwnerId(id);
    }

    @Override
    public RestuarantDto addToFavourites(Long id, User user) throws Exception {
        Restaurant restaurant = getRestaurantById(id);

        RestuarantDto dto = new RestuarantDto();
        dto.setId(id);
        dto.setTitle(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setImages(restaurant.getImages());

        if(user.getFavoriteFoodList().contains(dto)) {
            user.getFavoriteFoodList().remove(dto);
        }else{
            user.getFavoriteFoodList().add(dto);
        }

        userRepository.save(user);

        return dto;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
}
