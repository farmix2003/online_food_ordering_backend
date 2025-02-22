package com.farmix.controller;

import com.farmix.entity.Image;
import com.farmix.entity.ImageType;
import com.farmix.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Image> uploadNewImage(@RequestParam("file") MultipartFile file,
                                                @RequestParam("imageType") String imageType) {
        Image image;
        try {
            image = imageService.uploadImage(file, ImageType.valueOf(imageType.toUpperCase()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return new ResponseEntity<>(image, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) {
        Image image = imageService.viewImageById(id);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<ByteArrayResource> viewImage(@PathVariable Long id) {
        Image image = imageService.viewImageById(id);

        ByteArrayResource resource = new ByteArrayResource(image.getData());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageService.viewAllImages());
    }
}
