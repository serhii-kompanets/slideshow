package org.slideshow.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SlideShowDto {
    private final Long id;
    private final String name;
    private final String description;
    private final List<SlideDto> slides;

    @JsonCreator
    public SlideShowDto(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("slides") List<SlideDto> slides)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slides = slides;
    }

    @JsonGetter
    public Long getId() {
        return id;
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
    public List<SlideDto> getSlides() {
        return slides;
    }
}
