package org.slideshow.repositories;

import org.slideshow.entities.ProofOfPlayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProofOfPlayRepository extends JpaRepository<ProofOfPlayEntity, Long> {
}
