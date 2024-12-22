package org.slideshow.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slideshow.dtos.ImageDto;
import org.slideshow.entities.ImageEntity;
import org.slideshow.entities.SlideshowEntity;
import org.slideshow.entities.SlideshowImageEntity;
import org.slideshow.enums.ImageType;
import org.slideshow.exceptions.ImageNotFoundException;
import org.slideshow.exceptions.MimeTypeNotSupportedException;
import org.slideshow.exceptions.UploadFileBrokenException;
import org.slideshow.repositories.ImageRepository;
import org.slideshow.service.impl.ImageServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ImageServiceTest {
    private ImageService imageService;
    private ImageRepository mockImageRepository;

    @BeforeEach
    void setUp() {
        mockImageRepository = mock(ImageRepository.class);
        imageService = new ImageServiceImpl(mockImageRepository);
    }

    @Test
    void test_try_to_save_image_expected_success_result() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-image.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getBytes()).thenReturn(new byte[10]);

        ImageEntity image = getImageEntity();

        when(mockImageRepository.save(any(ImageEntity.class))).thenReturn(image);

        String savedImageUrl = imageService.addImage(mockFile);

        assertThat(savedImageUrl).isNotBlank();
        assertThat(savedImageUrl).isEqualTo("/images/1");
        verify(mockImageRepository, times(1)).save(any(ImageEntity.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"image/jpeg", "image/png", "image/webp"})
    void test_try_to_save_image_with_different_support_mime_types_expected_success_result(String mimeType) throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-image.jpg");
        when(mockFile.getContentType()).thenReturn(mimeType);
        when(mockFile.getBytes()).thenReturn(new byte[10]);

        ImageEntity image = getImageEntity(ImageType.getMimeType(mimeType));

        when(mockImageRepository.save(any(ImageEntity.class))).thenReturn(image);

        String savedImageUrl = imageService.addImage(mockFile);

        assertThat(savedImageUrl).isNotBlank();
        verify(mockImageRepository, times(1)).save(any(ImageEntity.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"video/mp4", "", "application/json", "txt", "doc", ".custom_user_type", "*.*", "*/*", "qwerty12345"})
    void test_try_to_save_image_unsupported_mime_types_expected_throws_exceptions(String mimeType) throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-image.jpg");
        when(mockFile.getContentType()).thenReturn(mimeType);
        when(mockFile.getBytes()).thenReturn(new byte[10]);

        assertThrows(MimeTypeNotSupportedException.class, () -> imageService.addImage(mockFile));
        verify(mockImageRepository, never()).save(any(ImageEntity.class));
    }

    @Test
    void test_try_save_broken_image_expected_throw_exception() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-image.jpg");
        when(mockFile.getBytes()).thenThrow(new IOException("Test IO exception"));
        when(mockFile.getContentType()).thenReturn("image/jpeg");

        UploadFileBrokenException exception = assertThrows(UploadFileBrokenException.class, () -> imageService.addImage(mockFile));

        assertThat(exception.getMessage()).isNotBlank();
        verify(mockImageRepository, never()).save(any(ImageEntity.class));
    }

    @Test
    void test_fetch_image_data_by_id_expected_success_result() {
        ImageEntity image = getImageEntity();

        when(mockImageRepository.findById(1L)).thenReturn(Optional.of(image));

        byte[] imageData = imageService.getImageData(1L);

        assertThat(imageData).isNotNull();
        verify(mockImageRepository, times(1)).findById(1L);
    }

    @Test
    void test_trying_to_fetch_image_data_which_does_not_exist_expected_throw_exception() {
        when(mockImageRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(ImageNotFoundException.class, () -> imageService.getImageData(1L));

        assertEquals("Image not found with ID: 1", exception.getMessage());
        verify(mockImageRepository, times(1)).findById(1L);
    }


    @Test
    void test_trying_delete_image_expected_success_result() {
        ImageEntity image = getImageEntity();

        when(mockImageRepository.findById(1L)).thenReturn(Optional.of(image));
        doNothing().when(mockImageRepository).deleteById(1L);

        imageService.deleteImage(1L);

        verify(mockImageRepository, times(1)).deleteById(1L);
    }

    @Test
    void test_trying_delete_image_failure_expected() {
        when(mockImageRepository.findById(9999L)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> imageService.deleteImage(9999L));

        verify(mockImageRepository, times(1)).findById(9999L);
    }

    @Test
    void test_get_image_properties_by_id_expected_success_result() {
        ImageEntity image = getImageEntity();

        when(mockImageRepository.findById(1L)).thenReturn(Optional.of(image));

        ImageDto imageDto = imageService.getImage(1L);
        assertThat(imageDto).isNotNull();
        assertThat(imageDto.getId()).isEqualTo(1L);
        assertThat(imageDto.getFileName()).isEqualTo("test-image.jpg");
        assertThat(imageDto.getUrl()).isEqualTo("/images/1");

        verify(mockImageRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchImagesWithSlideshows() {
        String keyword = "test";
        Integer duration = 120;

        ImageEntity image1 = new ImageEntity();
        image1.setId(1L);
        image1.setFilename("test-image.jpg");
        image1.setDuration(duration);

        ImageEntity image2 = new ImageEntity();
        image2.setId(2L);
        image2.setFilename("test-image.jpg");
        image2.setDuration(duration);

        SlideshowImageEntity slideshowImage = new SlideshowImageEntity();
        slideshowImage.setId(1L);
        slideshowImage.setImage(image1);

        SlideshowImageEntity slideshowImage2 = new SlideshowImageEntity();
        slideshowImage2.setId(2L);
        slideshowImage2.setImage(image2);

        SlideshowEntity slideshow1 = new SlideshowEntity();
        slideshow1.setId(1L);
        slideshow1.setName("Slideshow 1");
        slideshow1.setImages(List.of(slideshowImage, slideshowImage2));

        List<Object[]> mockResult = Arrays.asList(new Object[]{image1, slideshow1}, new Object[] {image2, slideshow1});

        when(mockImageRepository.searchImagesWithSlideshows(keyword, duration))
                .thenReturn(mockResult);

        List<Map<String, Object>> images = imageService.searchImage(keyword, duration);

        assertThat(images.size()).isEqualTo(2);
        assertThat(images.get(0).get("image")).isNotNull();
    }

    private static @NotNull ImageEntity getImageEntity() {
        return getImageEntity(ImageType.JPEG);
    }

    private static @NotNull ImageEntity getImageEntity(ImageType imageType) {
        ImageEntity image = new ImageEntity();
        image.setId(1L);
        image.setType(ImageType.JPEG);
        image.setFilename("test-image.jpg");
        image.setData(new byte[10]);
        image.setSize(10L);

        return image;
    }
}
