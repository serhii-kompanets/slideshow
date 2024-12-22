CREATE TABLE slideshow.slideshow (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           description TEXT,
                           created_at TIMESTAMP DEFAULT NOW(),
                           updated_at TIMESTAMP DEFAULT NOW()
);

-- CREATE SEQUENCE slideshow.slideshow_id_seq;