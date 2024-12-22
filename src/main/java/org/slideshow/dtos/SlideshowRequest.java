package org.slideshow.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SlideshowRequest {
    private final String name;
    private final String description;
    private final List<SlideRequest> slides;

    @JsonCreator
    public SlideshowRequest(@JsonProperty("name") String name,
                            @JsonProperty("description") String description,
                            @JsonProperty("slides") List<SlideRequest> slides)
    {
        this.name = name;
        this.description = description;
        this.slides = slides;
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    @JsonGetter
    public String getDescription() {
        return description;
    }

    @JsonGetter
    public List<SlideRequest> getSlides() {
        return slides;
    }
}
