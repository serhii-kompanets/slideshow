package org.slideshow.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SlideRequest {
    private final Long imageId;
    private final Integer duration;
    private final Integer position;

    @JsonCreator
    public SlideRequest(@JsonProperty("imageId") Long imageId, @JsonProperty("duration") Integer duration, @JsonProperty("position") Integer position) {
        this.imageId = imageId;
        this.duration = duration;
        this.position = position;
    }

    @JsonGetter
    public Long getImageId() {
        return imageId;
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
