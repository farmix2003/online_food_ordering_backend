package com.farmix.service;

import com.farmix.entity.Image;
import com.farmix.entity.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ImageService {

    Image uploadImage(MultipartFile file, ImageType imageType) throws IOException;
    List<Image> viewAllImages();
    Image viewImageById(Long id);
    String generateUrl(Long id);
}
