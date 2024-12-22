package org.slideshow.repositories;

import org.slideshow.entities.SlideshowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideShowRepository extends JpaRepository<SlideshowEntity, Long> {
}
