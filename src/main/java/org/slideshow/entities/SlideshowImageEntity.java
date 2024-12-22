package org.slideshow.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "slideshow_image")
public class SlideshowImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slideshow_image_id_gen")
    @SequenceGenerator(name = "slideshow_image_id_gen", sequenceName = "slideshow_image_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slideshow_id", nullable = false)
    private SlideshowEntity slideshow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private ImageEntity image;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Integer position;

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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
