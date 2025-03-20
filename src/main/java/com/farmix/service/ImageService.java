package com.farmix.service;

import com.farmix.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    Image uploadImage(MultipartFile file, Long restaurantId, Long menuId) throws IOException;
    List<Image> viewAllImages();
    Image viewImageById(Long id);
    String generateUrl(Long id);
}
