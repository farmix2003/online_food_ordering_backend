package com.farmix.service.serviceImpl;

import com.farmix.entity.Image;
import com.farmix.entity.ImageType;
import com.farmix.exception.ImageNotFoundException;
import com.farmix.repository.ImageRepository;
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

    private static final String BASE_URL = "http://localhost:8080/api/image/view";

    @Override
    public Image uploadImage(MultipartFile file, ImageType imageType) throws IOException {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setData(file.getBytes());
        image.setCreatedAt(LocalDateTime.now());
        image.setImageType(imageType);
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
