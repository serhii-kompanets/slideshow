package org.slideshow.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageDto {
    private final long id;
    private final String fileName;
    private final String url;
    private final long duration;
    private final int width;
    private final int height;

    @JsonCreator
    public ImageDto(@JsonProperty("id") long id,
                    @JsonProperty("filename") String fileName,
                    @JsonProperty("url") String url,
                    @JsonProperty("duration") long duration,
                    @JsonProperty("width") int width,
                    @JsonProperty("height") int height
    ) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.duration = duration;
        this.width = width;
        this.height = height;
    }

    @JsonGetter
    public long getId() {
        return id;
    }

    @JsonGetter
    public String getFileName() {
        return fileName;
    }

    @JsonGetter
    public String getUrl() {
        return url;
    }

    @JsonGetter
    public long getDuration() {
        return duration;
    }

    @JsonGetter
    public int getWidth() {
        return width;
    }

    @JsonGetter
    public int getHeight() {
        return height;
    }
}
