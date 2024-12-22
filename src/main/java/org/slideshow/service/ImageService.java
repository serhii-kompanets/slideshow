package org.slideshow.service;

import org.slideshow.dtos.ImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String addImage(MultipartFile file);
    byte[] getImageData(long id);
    ImageDto getImage(long id);
    void deleteImage(long id);
}
