package org.slideshow.controllers;

import org.slideshow.dtos.ImageDto;
import org.slideshow.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(path = "/addImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(imageService.addImage(file));
    }

    @GetMapping(path = "/images/search")
    public ResponseEntity<List<String>> search() {
        return null;
    }

    @DeleteMapping(path = "/deleteImage/{id}")
    public void delete(@PathVariable long id) {
        imageService.deleteImage(id);
    }

    @GetMapping(path = "/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(IMAGE_JPEG_VALUE))
                .body(imageService.getImageData(id));
    }

    @GetMapping(path = "/image/{id}/properties")
    public ResponseEntity<ImageDto> getImageProperties(@PathVariable Long id) {
        return ResponseEntity.ok(imageService.getImage(id));
    }
}
