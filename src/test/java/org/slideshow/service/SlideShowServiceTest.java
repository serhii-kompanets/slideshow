package org.slideshow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slideshow.dtos.SlideDto;
import org.slideshow.dtos.SlideRequest;
import org.slideshow.dtos.SlideShowDto;
import org.slideshow.dtos.SlideshowRequest;
import org.slideshow.entities.ImageEntity;
import org.slideshow.entities.ProofOfPlayEntity;
import org.slideshow.entities.SlideshowEntity;
import org.slideshow.entities.SlideshowImageEntity;
import org.slideshow.enums.ImageType;
import org.slideshow.exceptions.ImageNotFoundException;
import org.slideshow.exceptions.SlideshowNotFoundException;
import org.slideshow.repositories.ImageRepository;
import org.slideshow.repositories.ProofOfPlayRepository;
import org.slideshow.repositories.SlideShowRepository;
import org.slideshow.repositories.SlideshowImageRepository;
import org.slideshow.service.impl.SlideShowServiceImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class SlideShowServiceTest {
    private SlideShowRepository mockSlideshowRepository;

    private ImageRepository mockImageRepository;

    private SlideshowImageRepository mockSlideshowImageRepository;

    private ProofOfPlayRepository mockProofOfPlayRepository;

    private SlideShowService slideShowService;

    @BeforeEach
    void setUp() {
        mockImageRepository = mock(ImageRepository.class);
        mockSlideshowRepository = mock(SlideShowRepository.class);
        mockSlideshowImageRepository = mock(SlideshowImageRepository.class);
        mockProofOfPlayRepository = mock(ProofOfPlayRepository.class);
        slideShowService = new SlideShowServiceImpl(mockSlideshowRepository, mockImageRepository, mockProofOfPlayRepository);
    }

    @Test
    void test_create_simple_slideshow_expected_success_result() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setName("Test Slideshow");

        when(mockSlideshowRepository.save(any(SlideshowEntity.class))).thenReturn(slideshow);

        SlideShowDto createdSlideshow = slideShowService.createSlideShow(new SlideshowRequest("Test Slideshow", "", Collections.emptyList()));

        assertThat(createdSlideshow).isNotNull();
        assertThat(createdSlideshow.getName()).isEqualTo("Test Slideshow");
        assertThat(createdSlideshow.getDescription()).isBlank();
        assertThat(createdSlideshow.getSlides().size()).isEqualTo(0);
        verify(mockSlideshowRepository, times(1)).save(any(SlideshowEntity.class));
    }

    @Test
    void test_trying_create_slideshow_with_null_request_expected_throw_exception() {
        assertThrows(IllegalArgumentException.class, () -> slideShowService.createSlideShow(null));

        verify(mockSlideshowRepository, never()).save(any(SlideshowEntity.class));
    }

    @Test
    void test_create_slideshow_with_images_expected_success_result() {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setFilename("filename.jpg");
        imageEntity.setType(ImageType.JPEG);

        SlideshowImageEntity slideshowImage = new SlideshowImageEntity();
        slideshowImage.setImage(imageEntity);

        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setId(1L);
        slideshow.setName("Test Slideshow");
        slideshow.setDescription("Test Slideshow description");
        slideshow.setImages(List.of(slideshowImage));

        when(mockSlideshowRepository.save(any(SlideshowEntity.class))).thenReturn(slideshow);
        when(mockImageRepository.findById(1L)).thenReturn(Optional.of(imageEntity));

        SlideShowDto testSlideshow = slideShowService.createSlideShow(
                new SlideshowRequest("Test slideshow", "", List.of(new SlideRequest(1L, 5, 1)))
        );

        assertThat(testSlideshow).isNotNull();
        assertThat(testSlideshow.getName()).isEqualTo("Test Slideshow");
        assertThat(testSlideshow.getDescription()).isEqualTo("Test Slideshow description");
        assertThat(testSlideshow.getSlides().size()).isEqualTo(1);

        verify(mockImageRepository, times(1)).findById(any(Long.class));
        verify(mockSlideshowRepository, times(1)).save(any(SlideshowEntity.class));
    }

    @Test
    void test_try_to_create_slideshow_with_image_which_does_not_exists_expected_throw_exception() {
        when(mockImageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> slideShowService.createSlideShow(
                new SlideshowRequest("Test", "", List.of(new SlideRequest(99999L, 5, 1)))
        ));

        verify(mockImageRepository, never()).save(any(ImageEntity.class));
        verify(mockSlideshowImageRepository, never()).save(any(SlideshowImageEntity.class));
    }

    @Test
    void test_get_images_in_order_expected_replaced_entities_success() {
        ImageEntity imageEntity1 = new ImageEntity();
        imageEntity1.setFilename("filename.jpg");
        imageEntity1.setType(ImageType.JPEG);
        imageEntity1.setId(1L);
        imageEntity1.setCreatedAt(LocalDate.of(2024, 10, 12).atStartOfDay());

        ImageEntity imageEntity2 = new ImageEntity();
        imageEntity2.setFilename("filename.jpg");
        imageEntity2.setType(ImageType.JPEG);
        imageEntity2.setId(1000L);
        imageEntity2.setCreatedAt(LocalDate.of(2024, 2, 12).atStartOfDay());

        SlideshowImageEntity slideshowImage1 = new SlideshowImageEntity();
        slideshowImage1.setPosition(1);
        slideshowImage1.setImage(imageEntity1);

        SlideshowImageEntity slideshowImage2 = new SlideshowImageEntity();
        slideshowImage2.setPosition(2);
        slideshowImage2.setImage(imageEntity2);

        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setId(1L);
        slideshow.setName("Test Slideshow");
        slideshow.setImages(List.of(slideshowImage1, slideshowImage2));

        when(mockSlideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));

        List<SlideDto> images = slideShowService.getImagesInOrder(1L);

        assertThat(images.size()).isEqualTo(2);

        assertThat(images.get(0).getImageId()).isEqualTo(imageEntity2.getId());
        assertThat(images.get(0).getPosition()).isEqualTo(2);
        assertThat(images.get(1).getImageId()).isEqualTo(imageEntity1.getId());
        assertThat(images.get(1).getPosition()).isEqualTo(1);
    }

    @Test
    void testRecordProofOfPlaySuccess() {
        ImageEntity image = new ImageEntity();
        image.setId(1L);

        ImageEntity nextImage = new ImageEntity();
        nextImage.setId(2L);

        SlideshowImageEntity slideshowImage1 = new SlideshowImageEntity();
        slideshowImage1.setId(1L);
        slideshowImage1.setPosition(1);
        slideshowImage1.setImage(image);

        SlideshowImageEntity slideshowImage2 = new SlideshowImageEntity();
        slideshowImage2.setId(2L);
        slideshowImage2.setPosition(2);
        slideshowImage2.setImage(nextImage);

        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setId(1L);
        slideshow.setName("Test Slideshow");
        slideshow.setImages(List.of(slideshowImage1, slideshowImage2));

        when(mockSlideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));
        when(mockImageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(mockImageRepository.findById(2L)).thenReturn(Optional.of(nextImage));

        slideShowService.recordOfPlay(1L, 1L);

        verify(mockProofOfPlayRepository, times(1)).save(any(ProofOfPlayEntity.class));
    }

    @Test
    void test_proof_of_play_when_image_not_found_expected_throw_exception() {
        when(mockImageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SlideshowNotFoundException.class, () -> slideShowService.recordOfPlay(1L, 2L));

        verify(mockProofOfPlayRepository, never()).save(any(ProofOfPlayEntity.class));
    }

    @Test
    void test_proof_of_play_when_current_image_not_found_expected_throw_exception() {
        ImageEntity image = new ImageEntity();
        image.setId(1L);

        ImageEntity nextImage = new ImageEntity();
        nextImage.setId(2L);

        SlideshowImageEntity slideshowImage1 = new SlideshowImageEntity();
        slideshowImage1.setId(1L);
        slideshowImage1.setPosition(1);
        slideshowImage1.setImage(image);

        SlideshowImageEntity slideshowImage2 = new SlideshowImageEntity();
        slideshowImage2.setId(2L);
        slideshowImage2.setPosition(2);
        slideshowImage2.setImage(nextImage);

        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setId(1L);
        slideshow.setName("Test Slideshow");
        slideshow.setImages(List.of(slideshowImage1, slideshowImage2));

        when(mockSlideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));
        when(mockImageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(mockImageRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> slideShowService.recordOfPlay(1L, 2L));

        verify(mockProofOfPlayRepository, never()).save(any(ProofOfPlayEntity.class));
    }

    @Test
    void test_delete_slideshow_success() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setId(1L);

        when(mockSlideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));

        slideShowService.deleteSlideShow(1L);

        verify(mockSlideshowRepository, times(1)).deleteById(1L);
    }
}
