package org.slideshow.service.impl;

import org.slideshow.dtos.ImageDto;
import org.slideshow.dtos.SlideDto;
import org.slideshow.dtos.SlideShowDto;
import org.slideshow.entities.ImageEntity;
import org.slideshow.entities.SlideshowEntity;
import org.slideshow.enums.ImageType;
import org.slideshow.exceptions.ImageNotFoundException;
import org.slideshow.exceptions.UploadFileBrokenException;
import org.slideshow.repositories.ImageRepository;
import org.slideshow.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private static final String IMAGE_URL_TEMPLATE = "/images/%s";

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public String addImage(MultipartFile file) {
        ImageEntity image = new ImageEntity();
        image.setFilename(file.getOriginalFilename());
        image.setType(ImageType.getMimeType(file.getContentType()));
        image.setSize(file.getSize());

        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            if (bufferedImage != null) {
                image.setWidth(bufferedImage.getWidth());
                image.setHeight(bufferedImage.getHeight());
            }
            image.setData(file.getBytes());
        } catch (IOException e) {
            throw new UploadFileBrokenException("Could not upload file. The reason is: " + e.getMessage());
        }

        ImageEntity savedImage = imageRepository.save(image);

        return String.format(IMAGE_URL_TEMPLATE, savedImage.getId());
    }

    @Override
    public void deleteImage(long id) {
        Optional<ImageEntity> imageEntityOptional = imageRepository.findById(id);

        if (imageEntityOptional.isEmpty()) {
            throw new ImageNotFoundException("Image not found or already deleted");
        }

        imageRepository.deleteById(id);
    }

    @Override
    public byte[] getImageData(long id) {
        ImageEntity image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image not found with ID: " + id));

        return image.getData();
    }

    @Override
    public ImageDto getImage(long id) {
        ImageEntity image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image not found with ID: " + id));

        return convertToDto(image);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> searchImage(String keyword, Integer duration) {
        List<Object[]> results = imageRepository.searchImagesWithSlideshows(keyword, duration);

        Map<ImageDto, List<SlideShowDto>> groupedResults = new HashMap<>();
        for (Object[] row : results) {
            ImageEntity image = (ImageEntity) row[0];
            SlideshowEntity slideshow = (SlideshowEntity) row[1];
            groupedResults.computeIfAbsent(convertToDto(image), k -> new ArrayList<>()).add(convertToDto(slideshow));
        }

        return groupedResults.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("image", entry.getKey());
            map.put("slideshows", entry.getValue());
            return map;
        }).toList();
    }

    private static ImageDto convertToDto(ImageEntity image) {
        return new ImageDto(
                image.getId(),
                image.getFilename(),
                String.format(IMAGE_URL_TEMPLATE, image.getId()),
                image.getDuration(),
                image.getWidth(),
                image.getHeight()
        );
    }

    private static SlideShowDto convertToDto(SlideshowEntity slideshow) {
        return slideshow != null ? new SlideShowDto(
                slideshow.getId(),
                slideshow.getName(),
                slideshow.getDescription(),
                slideshow.getImages().stream().map(slideshowImageEntity -> new SlideDto(
                        slideshowImageEntity.getImage().getId(),
                        slideshowImageEntity.getImage().getFilename(),
                        String.format(IMAGE_URL_TEMPLATE, slideshowImageEntity.getImage().getId()),
                        slideshowImageEntity.getImage().getDuration(),
                        slideshowImageEntity.getPosition()
                )).toList()
        ) : null;
    }
}
