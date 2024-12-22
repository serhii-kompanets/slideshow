package org.slideshow.repositories;

import org.junit.jupiter.api.Test;
import org.slideshow.entities.ImageEntity;
import org.slideshow.enums.ImageType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ImagesRepositoryTest extends AbstractDataIntegrationTest {
    @Autowired
    private ImageRepository imageRepository;

    @Test
    void test_save_image_without_blob_object_expected_success_result() {
        ImageEntity image = new ImageEntity();
        image.setFilename("test.jpg");
        image.setType(ImageType.JPEG);
        image.setSize(1024L);
        image.setHeight(800);
        image.setWidth(600);

        ImageEntity savedImage = imageRepository.save(image);

        assertThat(savedImage).isNotNull();
        assertThat(savedImage.getFilename()).isEqualTo("test.jpg");
        assertThat(savedImage.getType()).isEqualTo(ImageType.JPEG);
        assertThat(savedImage.getSize()).isEqualTo(1024L);
        assertThat(savedImage.getHeight()).isEqualTo(800);
        assertThat(savedImage.getWidth()).isEqualTo(600);
    }

    @Test
    void test_find_by_id_success_result() {
        ImageEntity image = new ImageEntity();
        image.setFilename("test.jpg");
        image.setType(ImageType.JPEG);
        image.setSize(1024L);
        image.setHeight(800);
        image.setWidth(600);

        ImageEntity savedImage = imageRepository.save(image);
        Optional<ImageEntity> foundImage = imageRepository.findById(savedImage.getId());

        assertThat(foundImage).isPresent();
        assertThat(savedImage.getId()).isEqualTo(foundImage.get().getId());
    }

    @Test
    void test_find_by_id_not_found_result() {
        Optional<ImageEntity> foundImage = imageRepository.findById(999L);
        assertThat(foundImage).isNotPresent();
    }

    @Test
    void test_delete_by_id_success_result() {
        ImageEntity image = new ImageEntity();
        image.setFilename("test.jpg");
        image.setType(ImageType.JPEG);
        image.setSize(1024L);
        image.setHeight(800);
        image.setWidth(600);

        ImageEntity savedImage = imageRepository.save(image);
        imageRepository.deleteById(savedImage.getId());

        Optional<ImageEntity> foundImage = imageRepository.findById(savedImage.getId());

        assertThat(foundImage).isNotPresent();
    }

    @Test
    void test_delete_by_id_not_found() {
        assertDoesNotThrow(() -> imageRepository.deleteById(999L));
    }
}
