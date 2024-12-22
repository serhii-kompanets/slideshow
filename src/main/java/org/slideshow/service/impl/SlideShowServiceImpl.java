package org.slideshow.service.impl;

import org.slideshow.dtos.SlideDto;
import org.slideshow.dtos.SlideShowDto;
import org.slideshow.dtos.SlideshowRequest;
import org.slideshow.entities.ImageEntity;
import org.slideshow.entities.ProofOfPlayEntity;
import org.slideshow.entities.SlideshowEntity;
import org.slideshow.entities.SlideshowImageEntity;
import org.slideshow.exceptions.ImageNotFoundException;
import org.slideshow.exceptions.SlideshowNotFoundException;
import org.slideshow.repositories.ImageRepository;
import org.slideshow.repositories.ProofOfPlayRepository;
import org.slideshow.repositories.SlideShowRepository;
import org.slideshow.service.SlideShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class SlideShowServiceImpl implements SlideShowService {
    private final SlideShowRepository slideShowRepository;
    private final ImageRepository imageRepository;
    private final ProofOfPlayRepository proofOfPlayRepository;

    private static final String IMAGE_ID_URL = "/images/%s";
    private static final String SLIDE_NOT_FOUND_ERROR_MESSAGE = "Slideshow not found with ID:";

    @Autowired
    public SlideShowServiceImpl(SlideShowRepository slideShowRepository, ImageRepository imageRepository, ProofOfPlayRepository proofOfPlayRepository) {
        this.slideShowRepository = slideShowRepository;
        this.imageRepository = imageRepository;
        this.proofOfPlayRepository = proofOfPlayRepository;
    }

    @Override
    @Transactional
    public SlideShowDto createSlideShow(SlideshowRequest slideshowRequest) {
        if (slideshowRequest == null) {
            throw new IllegalArgumentException("slideshowRequest cannot be null");
        }

        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setName(slideshowRequest.getName());
        slideshow.setDescription(slideshowRequest.getDescription());

        List<SlideshowImageEntity> slides = slideshowRequest.getSlides()
                .stream()
                .map(slideRequest -> {
                    ImageEntity image = imageRepository.findById(slideRequest.getImageId())
                            .orElseThrow(() -> new ImageNotFoundException("Image not found with ID: " + slideRequest.getImageId()));

                    SlideshowImageEntity slide = new SlideshowImageEntity();
                    slide.setImage(image);
                    slide.setDuration(slideRequest.getDuration());
                    slide.setPosition(slideRequest.getPosition());
                    slide.setSlideshow(slideshow);

                    return slide;
                }).toList();

        slideshow.setImages(slides);
        SlideshowEntity saved = slideShowRepository.save(slideshow);

        return convertSlideShowToDto(saved);
    }

    @Override
    public SlideShowDto getSlideShow(Long slideShowId) {
        SlideshowEntity slideshowEntity = slideShowRepository
                .findById(slideShowId)
                .orElseThrow(() -> new SlideshowNotFoundException(SLIDE_NOT_FOUND_ERROR_MESSAGE + slideShowId));
        return convertSlideShowToDto(slideshowEntity);
    }

    private static SlideShowDto convertSlideShowToDto(SlideshowEntity slideshowEntity) {
        return new SlideShowDto(
                slideshowEntity.getId(),
                slideshowEntity.getName(),
                slideshowEntity.getDescription(),
                slideshowEntity.getImages().stream().map(convertSlideShowToDto()).toList()
                );
    }

    private static Function<SlideshowImageEntity, SlideDto> convertSlideShowToDto() {
        return slideshowImage -> new SlideDto(
            slideshowImage.getImage().getId(),
            String.format(IMAGE_ID_URL, slideshowImage.getImage().getId()),
            slideshowImage.getDuration(),
            slideshowImage.getPosition()
        );
    }

    @Override
    public void deleteSlideShow(long id) {
        slideShowRepository.findById(id).orElseThrow(() -> new SlideshowNotFoundException(SLIDE_NOT_FOUND_ERROR_MESSAGE + id));
        slideShowRepository.deleteById(id);

    }

    @Override
    @Transactional
    public List<SlideDto> getImagesInOrder(Long slideshowId) {
        SlideshowEntity slideshowEntity = slideShowRepository.findById(slideshowId)
                .orElseThrow(() -> new SlideshowNotFoundException(SLIDE_NOT_FOUND_ERROR_MESSAGE + slideshowId));

        return slideshowEntity.getImages().stream()
                .sorted(Comparator.comparing(slideshowImageEntity -> slideshowImageEntity.getImage().getCreatedAt()))
                .map(convertSlideShowToDto())
                .toList();
    }

    @Override
    public void recordOfPlay(Long slideshowId, Long imageId) {
        SlideshowEntity slideshowEntity = slideShowRepository.findById(slideshowId)
                .orElseThrow(() -> new SlideshowNotFoundException(SLIDE_NOT_FOUND_ERROR_MESSAGE + slideshowId));

        ImageEntity imageEntity = imageRepository
                .findById(imageId).orElseThrow(() -> new ImageNotFoundException("Image not found with ID: " + imageId));

        Optional<SlideshowImageEntity> currentSlide = slideshowEntity.getImages().stream()
                .filter(slideshowImageEntity -> slideshowImageEntity.getId().equals(imageId)).findFirst();

        if (currentSlide.isEmpty()) {
            throw new IllegalArgumentException("Image does not belong to the slideshow with ID: " + slideshowId);
        }

        List<SlideshowImageEntity> orderedSlides = slideshowEntity.getImages().stream()
                .sorted(Comparator.comparing(SlideshowImageEntity::getPosition))
                .toList();

        int currentIndex = orderedSlides.indexOf(currentSlide.get());
        if (currentIndex == -1 || currentIndex == orderedSlides.size() - 1) {
            throw new IllegalArgumentException("No next image available in the slideshow.");
        }

        SlideshowImageEntity nextSlide = orderedSlides.get(currentIndex + 1);

        ProofOfPlayEntity proofOfPlayEntity = new ProofOfPlayEntity();
        proofOfPlayEntity.setSlideshow(slideshowEntity);
        proofOfPlayEntity.setImage(imageEntity);
        proofOfPlayEntity.setNextImage(nextSlide.getImage());
        proofOfPlayEntity.setTimestamp(LocalDateTime.now());

        proofOfPlayRepository.save(proofOfPlayEntity);
    }
}
