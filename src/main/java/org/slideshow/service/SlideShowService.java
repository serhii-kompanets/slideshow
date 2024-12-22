package org.slideshow.service;

import org.slideshow.dtos.SlideDto;
import org.slideshow.dtos.SlideShowDto;
import org.slideshow.dtos.SlideshowRequest;

import java.util.List;

public interface SlideShowService {
    SlideShowDto createSlideShow(SlideshowRequest slideshowRequest);
    SlideShowDto getSlideShow(Long slideShowId);
    void deleteSlideShow();
    List<SlideDto> getImagesInOrder(Long slideshowId);
    void recordOfPlay(Long slideshowId, Long imageId);
}
