package org.slideshow.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.slideshow.enums.ImageType;

import java.time.LocalDateTime;

@Entity
@Table(name = "images")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_id_gen")
    @SequenceGenerator(name = "images_id_gen", sequenceName = "images_id_seq", allocationSize = 1)
    private long id;

    @Column
    private String filename;

    @Column
    @Enumerated(EnumType.STRING)
    private ImageType type;

    @Column
    private int duration;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    @Column
    private long size;

    @Column
    private int width;

    @Column
    private int height;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ImageType getType() {
        return type;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
