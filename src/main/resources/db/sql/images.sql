CREATE TABLE images (
                        id BIGSERIAL PRIMARY KEY,
                        filename VARCHAR(255) NOT NULL,
                        type VARCHAR(50) NOT NULL,
                        duration INT NOT NULL,
                        data oid NOT NULL,
                        size BIGINT,
                        width INT,
                        height INT,
                        created_at TIMESTAMP DEFAULT NOW()
);