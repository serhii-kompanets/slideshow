package org.slideshow.repositories;

import org.slideshow.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    @Query("SELECT i, s FROM ImageEntity i " +
            "LEFT JOIN SlideshowImageEntity si ON i.id = si.image.id " +
            "LEFT JOIN SlideshowEntity s ON si.slideshow.id = s.id " +
            "WHERE (:keyword IS NULL OR LOWER(i.filename) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:duration IS NULL OR i.duration = :duration)")
    List<Object[]> searchImagesWithSlideshows(@Param("keyword") String keyword, @Param("duration") Integer duration);
}
