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
import org.slideshow.repositories.ImageRepository;
import org.slideshow.repositories.ProofOfPlayRepository;
import org.slideshow.repositories.SlideShowRepository;
import org.slideshow.repositories.SlideshowImageRepository;
import org.slideshow.service.impl.SlideShowServiceImpl;

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
    void testCreateSlideshowSuccess() {
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
    void testCreateSlideshowEmptyName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> slideShowService.createSlideShow(null));

        assertThat(exception.getMessage()).isEqualTo("Name cannot be empty");
        verify(mockSlideshowRepository, never()).save(any(SlideshowEntity.class));
    }

    @Test
    void test_create_slideshow_with_images_expected_success_result() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setId(1L);

        ImageEntity image = new ImageEntity();
        image.setId(1L);

        when(mockSlideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));
        when(mockImageRepository.findById(1L)).thenReturn(Optional.of(image));

        slideShowService.createSlideShow(new SlideshowRequest("Test slideshow", "", List.of(new SlideRequest(1L, 5, 1))));

        verify(mockSlideshowImageRepository, times(1)).save(any(SlideshowImageEntity.class));
    }

/*

    @Test
    void testAddImageToSlideshowNotFound() {
        when(mockSlideshowRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> slideShowService.addImageToSlideshow(1L, 1L));

        assertEquals("Slideshow not found with ID: 1", exception.getMessage());
        verify(mockSlideshowImageRepository, never()).save(any(SlideshowImage.class));
    }

    @Test
    void testGetImagesInOrderSuccess() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setId(1L);

        SlideshowImageEntity slideshowImage1 = new SlideshowImageEntity();
        slideshowImage1.setPosition(1);

        SlideshowImageEntity slideshowImage2 = new SlideshowImageEntity();
        slideshowImage2.setPosition(2);

        when(mockSlideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));
        when(mockSlideshowImageRepository.findAllBySlideshowOrderByOrderIndexAsc(slideshow)).thenReturn(List.of(slideshowImage1, slideshowImage2));

        List<SlideDto> images = slideShowService.getImagesInOrder(1L);

        assertEquals(2, images.size());
        verify(mockSlideshowImageRepository, times(1)).findAllBySlideshowOrderByOrderIndexAsc(slideshow);
    }

    @Test
    void testRecordProofOfPlaySuccess() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setId(1L);

        ImageEntity image = new ImageEntity();
        image.setId(1L);

        ImageEntity nextImage = new ImageEntity();
        nextImage.setId(2L);

        when(mockSlideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));
        when(mockImageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(mockImageRepository.findById(2L)).thenReturn(Optional.of(nextImage));

        slideShowService.recordProofOfPlay(1L, 1L, 2L);

        verify(mockProofOfPlayRepository, times(1)).save(any(ProofOfPlay.class));
    }

    @Test
    void testRecordProofOfPlayImageNotFound() {
        when(mockImageRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> slideShowService.recordProofOfPlay(1L, 1L, 2L));

        assertEquals("Image not found with ID: 1", exception.getMessage());
        verify(mockProofOfPlayRepository, never()).save(any(ProofOfPlayEntity.class));
    }

    @Test
    void testDeleteSlideshowSuccess() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setId(1L);

        when(mockSlideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));

        slideShowService.deleteSlideshow(1L);

        verify(mockSlideshowImageRepository, times(1)).deleteBySlideshow(slideshow);
        verify(mockSlideshowRepository, times(1)).deleteById(1L);
    }

 */
}
