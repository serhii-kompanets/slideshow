package org.slideshow.service;

import org.slideshow.dtos.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImageService {
    String addImage(MultipartFile file);
    byte[] getImageData(long id);
    ImageDto getImage(long id);
    List<Map<String, Object>> searchImage(String keyword, Integer duration);
    void deleteImage(long id);
}
