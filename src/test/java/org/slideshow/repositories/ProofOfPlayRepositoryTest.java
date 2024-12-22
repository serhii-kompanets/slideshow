package org.slideshow.repositories;

import org.junit.jupiter.api.Test;
import org.slideshow.entities.ImageEntity;
import org.slideshow.entities.ProofOfPlayEntity;
import org.slideshow.entities.SlideshowEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProofOfPlayRepositoryTest extends AbstractDataIntegrationTest {
    @Autowired
    private ProofOfPlayRepository proofOfPlayRepository;

    @Autowired
    private SlideShowRepository slideshowRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    void test_save_proofOfPlay_expected_success_result() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setName("Test Slideshow");
        slideshow = slideshowRepository.save(slideshow);

        ImageEntity image = new ImageEntity();
        image.setFilename("test-image.jpg");
        image = imageRepository.save(image);

        ImageEntity nextImage = new ImageEntity();
        nextImage.setFilename("next-image.jpg");
        nextImage = imageRepository.save(nextImage);

        ProofOfPlayEntity proofOfPlay = new ProofOfPlayEntity();
        proofOfPlay.setSlideshow(slideshow);
        proofOfPlay.setImage(image);
        proofOfPlay.setNextImage(nextImage);
        proofOfPlay.setTimestamp(LocalDateTime.now());

        ProofOfPlayEntity savedProof = proofOfPlayRepository.save(proofOfPlay);

        assertThat(savedProof.getId()).isNotNull();
    }

    @Test
    void test_find_by_id_expected_success_result() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setName("Test Slideshow");
        slideshow = slideshowRepository.save(slideshow);

        ImageEntity image = new ImageEntity();
        image.setFilename("test-image.jpg");
        image = imageRepository.save(image);

        ImageEntity nextImage = new ImageEntity();
        nextImage.setFilename("next-image.jpg");
        nextImage = imageRepository.save(nextImage);

        ProofOfPlayEntity proofOfPlay = new ProofOfPlayEntity();
        proofOfPlay.setSlideshow(slideshow);
        proofOfPlay.setImage(image);
        proofOfPlay.setNextImage(nextImage);
        proofOfPlay.setTimestamp(LocalDateTime.now());
        proofOfPlay = proofOfPlayRepository.save(proofOfPlay);

        Optional<ProofOfPlayEntity> foundProof = proofOfPlayRepository.findById(proofOfPlay.getId());

        assertThat(foundProof.isPresent()).isTrue();
        assertThat(foundProof.get().getId()).isEqualTo(proofOfPlay.getId());
        assertThat(slideshow.getId()).isEqualTo(proofOfPlay.getSlideshow().getId());
        assertThat(image.getId()).isEqualTo(proofOfPlay.getImage().getId());
        assertThat(nextImage.getId()).isEqualTo(proofOfPlay.getNextImage().getId());
    }

    @Test
    void test_find_by_id_expected_not_found_result() {
        Optional<ProofOfPlayEntity> foundProof = proofOfPlayRepository.findById(999L);

        assertThat(foundProof.isPresent()).isFalse();
    }

    @Test
    void test_delete_by_id_expected_success_result() {
        SlideshowEntity slideshow = new SlideshowEntity();
        slideshow.setName("Test Slideshow");
        slideshow = slideshowRepository.save(slideshow);

        ImageEntity image = new ImageEntity();
        image.setFilename("test-image.jpg");
        image = imageRepository.save(image);

        ImageEntity nextImage = new ImageEntity();
        nextImage.setFilename("next-image.jpg");
        nextImage = imageRepository.save(nextImage);

        ProofOfPlayEntity proofOfPlay = new ProofOfPlayEntity();
        proofOfPlay.setSlideshow(slideshow);
        proofOfPlay.setImage(image);
        proofOfPlay.setNextImage(nextImage);
        proofOfPlay.setTimestamp(LocalDateTime.now());
        proofOfPlay = proofOfPlayRepository.save(proofOfPlay);

        proofOfPlayRepository.deleteById(proofOfPlay.getId());

        Optional<ProofOfPlayEntity> foundProof = proofOfPlayRepository.findById(proofOfPlay.getId());

        assertThat(foundProof.isPresent()).isFalse();
    }

    @Test
    void test_delete_by_id_expected_not_found_result() {
        assertDoesNotThrow(() -> proofOfPlayRepository.deleteById(999L), "Deleting non-existing ID should not throw an exception");
    }

    @Test
    void test_try_to_save_invalid_ProofOfPlay_expected_throw_exception() {
        ProofOfPlayEntity invalidProofOfPlay = new ProofOfPlayEntity();

        assertThrows(DataIntegrityViolationException.class, () -> {
            proofOfPlayRepository.save(invalidProofOfPlay);
        });
    }
}
