CREATE TABLE proof_of_play (
                               id BIGSERIAL PRIMARY KEY,
                               slideshow_id BIGINT NOT NULL REFERENCES slideshow(id) ON DELETE CASCADE,
                               current_image_id BIGINT NOT NULL REFERENCES images(id) ON DELETE CASCADE,
                               next_image_id BIGINT NOT NULL REFERENCES images(id) ON DELETE CASCADE,
                               timestamp TIMESTAMP NOT NULL
);

-- CREATE SEQUENCE slideshow.proofofplay_id_seq;