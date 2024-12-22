package org.slideshow.repositories;

import org.junit.jupiter.api.Test;
import org.slideshow.entities.ImageEntity;
import org.slideshow.entities.SlideshowEntity;
import org.slideshow.entities.SlideshowImageEntity;
import org.slideshow.enums.ImageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SlideShowRepositoryTest extends AbstractDataIntegrationTest {
    @Autowired
    private SlideShowRepository slideshowRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    void test_try_to_save_slideshow_expected_success_result() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setName("Test Slideshow");
        slideshow.setDescription("This is a test slideshow");

        SlideshowEntity savedSlideshow = slideshowRepository.save(slideshow);

        assertThat(savedSlideshow).isNotNull();
        assertThat(savedSlideshow.getId()).isNotNull();
        assertThat(savedSlideshow.getName()).isEqualTo(slideshow.getName());
        assertThat(savedSlideshow.getDescription()).isEqualTo(slideshow.getDescription());
    }

    @Test
    void test_find_by_id_expected_success_result() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setName("Test Slideshow");

        SlideshowEntity savedSlideshow = slideshowRepository.save(slideshow);
        Optional<SlideshowEntity> foundSlideshow = slideshowRepository.findById(savedSlideshow.getId());

        assertTrue(foundSlideshow.isPresent());
        assertThat(foundSlideshow.get().getId()).isEqualTo(savedSlideshow.getId());
        assertThat(foundSlideshow.get().getName()).isEqualTo(slideshow.getName());
    }

    @Test
    void test_find_by_id_which_doesnot_exists_expected_not_found_result() {
        Optional<SlideshowEntity> foundSlideshow = slideshowRepository.findById(999L);
        assertFalse(foundSlideshow.isPresent());
    }

    @Test
    void test_try_to_delete_by_id_expected_success_result() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setName("Test Slideshow");

        SlideshowEntity savedSlideshow = slideshowRepository.save(slideshow);
        slideshowRepository.deleteById(savedSlideshow.getId());

        Optional<SlideshowEntity> foundSlideshow = slideshowRepository.findById(savedSlideshow.getId());
        assertFalse(foundSlideshow.isPresent());
    }

    @Test
    void test_try_to_delete_by_id_which_not_exists_expected_not_found_result() {
        assertDoesNotThrow(() -> slideshowRepository.deleteById(999L));
    }

    @Test
    void test_try_to_save_slideshow_with_images_expected_success_result() {
        ImageEntity image1 = new ImageEntity();
        image1.setFilename("image1.jpg");
        image1.setType(ImageType.JPEG);
        image1.setSize(1024L);
        image1.setHeight(800);
        image1.setWidth(600);

        ImageEntity image2 = new ImageEntity();
        image2.setFilename("image2.jpg");
        image2.setType(ImageType.JPEG);
        image2.setSize(2048L);
        image2.setHeight(1200);
        image2.setWidth(900);

        imageRepository.save(image1);
        imageRepository.save(image2);

        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setName("Test Slideshow");

        SlideshowImageEntity slide1 = new SlideshowImageEntity();
        slide1.setImage(image1);
        slide1.setDuration(5);
        slide1.setPosition(1);
        slide1.setSlideshow(slideshow);

        SlideshowImageEntity slide2 = new SlideshowImageEntity();
        slide2.setImage(image2);
        slide2.setDuration(7);
        slide2.setPosition(2);
        slide2.setSlideshow(slideshow);

        slideshow.setImages(List.of(slide1, slide2));

        SlideshowEntity savedSlideshow = slideshowRepository.save(slideshow);

        assertThat(savedSlideshow.getId()).isNotNull();
        assertThat(savedSlideshow.getImages()).hasSize(2);
    }

    @Test
    void test_try_to_save_invalid_slideshow_expected_throw_exception_result() {
        SlideshowEntity invalidSlideshow = new SlideshowEntity();

        assertThrows(DataIntegrityViolationException.class, () -> {
            slideshowRepository.save(invalidSlideshow);
        }, "Saving a slideshow without required fields should throw an exception");
    }

    @Test
    void test_try_to_save_multiple_slideshow_expected_success_result() {
        SlideshowEntity slideshow1 = new SlideshowEntity();
        slideshow1.setName("Slideshow 1");

        SlideshowEntity slideshow2 = new SlideshowEntity();
        slideshow2.setName("Slideshow 2");

        slideshowRepository.saveAll(List.of(slideshow1, slideshow2));

        List<SlideshowEntity> savedSlideshows = slideshowRepository.findAll();

        assertTrue(savedSlideshows.size() >= 2, "At least 2 slideshows should be present in the database");
    }
}
