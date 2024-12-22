package org.slideshow.repositories;

import org.slideshow.entities.SlideshowImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideshowImageRepository extends JpaRepository<SlideshowImageEntity, Long> {
}
