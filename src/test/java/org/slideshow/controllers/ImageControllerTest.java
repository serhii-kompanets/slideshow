package org.slideshow.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slideshow.dtos.ErrorResponse;
import org.slideshow.exceptions.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImageControllerTest extends AbstractWebIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void test_add_new_image_expected_image_url_success_result() throws Exception {
        String filePath = "images/starwars.png";
        URL resource = getClass().getClassLoader().getResource(filePath);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + filePath);
        }
        File file = new File(resource.toURI());
        byte[] bytes = Files.readAllBytes(file.toPath());

        MockMultipartFile upladFile
                = new MockMultipartFile(
                "file",
                "starwars.png",
                MediaType.IMAGE_PNG_VALUE,
                bytes
        );

        String contentAsString = mockMvc.perform(multipart("/addImage")
                        .file(upladFile)
                        .header("Content-Type", "multipart/form-data"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isNotBlank();
        assertThat(contentAsString).isEqualTo("/images/1");
    }

    @Test
    void test_trying_upload_unsupported_file_expected_error_response() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        String contentAsString = mockMvc.perform(multipart("/addImage").file(file))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse singleObject = asSingleObject(contentAsString, ErrorResponse.class);

        assertThat(singleObject).isNotNull();
        assertThat(singleObject.getErrorType().getApplicationCode())
                .isEqualTo(ErrorType.UNSUPPORTED_MEDIA_TYPE.getApplicationCode());
        assertThat(singleObject.getErrorType().getMessage()).isEqualTo(ErrorType.UNSUPPORTED_MEDIA_TYPE.getMessage());
    }
}
