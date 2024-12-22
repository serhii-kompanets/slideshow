package org.slideshow.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SlideDto {
    private final Long imageId;
    private final String imageName;
    private final String imageUrl;
    private final Integer duration;
    private final Integer position;

    @JsonCreator
    public SlideDto(
            @JsonProperty("imageId") Long imageId, @JsonProperty("imageName") String imageName,
            @JsonProperty("imageUrl") String imageUrl, @JsonProperty("duration") Integer duration,
            @JsonProperty("position") Integer position
    )
    {
        this.imageId = imageId;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.duration = duration;
        this.position = position;
    }

    @JsonGetter
    public Long getImageId() {
        return imageId;
    }

    @JsonGetter
    public String getImageName() {
        return imageName;
    }

    @JsonGetter
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonGetter
    public Integer getDuration() {
        return duration;
    }

    @JsonGetter
    public Integer getPosition() {
        return position;
    }
}
