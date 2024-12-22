CREATE TABLE slideshow.slideshow_image (
                                 id BIGSERIAL PRIMARY KEY,
                                 slideshow_id BIGINT NOT NULL,
                                 image_id BIGINT NOT NULL,
                                 duration INT NOT NULL,
                                 position INT NOT NULL,
                                 FOREIGN KEY (slideshow_id) REFERENCES slideshow(id) ON DELETE CASCADE,
                                 FOREIGN KEY (image_id) REFERENCES images(id) ON DELETE CASCADE
);

-- CREATE SEQUENCE slideshow.slideshow_image_id_seq;