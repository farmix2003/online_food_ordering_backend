package com.farmix.service.serviceImpl;

import com.farmix.entity.Image;
import com.farmix.repository.ImageRepository;
import com.farmix.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
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


    private final String BASE_URL = "http://localhost:8080/api/image/view";

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setData(file.getBytes());
        image.setCreatedAt(LocalDateTime.now());
        return imageRepository.save(image);
    }

    @Override
    public List<Image> viewAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Optional<Image> viewImageById(Long id) {

        return imageRepository.findById(id);
    }

    @Override
    public String generateUrl(Long id) {
        return BASE_URL + "/"+id;
    }
}
