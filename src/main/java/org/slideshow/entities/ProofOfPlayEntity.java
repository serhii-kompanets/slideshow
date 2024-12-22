package org.slideshow.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "proof_of_play")
public class ProofOfPlayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proof_of_play_id_gen")
    @SequenceGenerator(name = "proof_of_play_id_gen", sequenceName = "proof_of_play_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slideshow_id", nullable = false)
    private SlideshowEntity slideshow;

    @ManyToOne
    @JoinColumn(name = "current_image_id", nullable = false)
    private ImageEntity image;

    @ManyToOne
    @JoinColumn(name = "next_image_id", nullable = false)
    private ImageEntity nextImage;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SlideshowEntity getSlideshow() {
        return slideshow;
    }

    public void setSlideshow(SlideshowEntity slideshow) {
        this.slideshow = slideshow;
    }

    public ImageEntity getImage() {
        return image;
    }

    public void setImage(ImageEntity image) {
        this.image = image;
    }

    public ImageEntity getNextImage() {
        return nextImage;
    }

    public void setNextImage(ImageEntity nextImage) {
        this.nextImage = nextImage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
