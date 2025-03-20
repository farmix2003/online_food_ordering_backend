package com.farmix.service.serviceImpl;

import com.farmix.entity.Image;
import com.farmix.entity.ImageType;
import com.farmix.entity.Menu;
import com.farmix.entity.Restaurant;
import com.farmix.exception.ImageNotFoundException;
import com.farmix.repository.ImageRepository;
import com.farmix.repository.MenuRepository;
import com.farmix.repository.RestaurantRepository;
import com.farmix.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    private static final String BASE_URL = "http://localhost:8080/api/image/view";

    @Override
    public Image uploadImage(MultipartFile file, Long restaurantId, Long menuId) throws IOException {

        if ((restaurantId == null && menuId == null) || (restaurantId != null && menuId != null)) {
            throw new IllegalArgumentException("Either restaurantId or menuId must be provided, but not both.");
        }

        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setData(file.getBytes());
        image.setCreatedAt(LocalDateTime.now());

        if (restaurantId != null) {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id " + restaurantId));
            image.setRestaurant(restaurant);
        } else {
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id " + menuId));
            image.setMenu(menu);
        }

        Image savedImage =  imageRepository.save(image);

        savedImage.setFileUrl(generateUrl(savedImage.getId()));

        return savedImage;
    }

    @Override
    public List<Image> viewAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image viewImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image not found with id "+id));
    }

    @Override
    public String generateUrl(Long id) {
        return BASE_URL + "/"+id;
    }
}
