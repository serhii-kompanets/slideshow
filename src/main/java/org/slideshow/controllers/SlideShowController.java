package org.slideshow.controllers;

import org.slideshow.dtos.SlideDto;
import org.slideshow.dtos.SlideShowDto;
import org.slideshow.dtos.SlideshowRequest;
import org.slideshow.service.SlideShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SlideShowController {
    private final SlideShowService slideShowService;

    @Autowired
    public SlideShowController(SlideShowService slideShowService) {
        this.slideShowService = slideShowService;
    }

    @PostMapping(path = "/addSlideshow")
    public ResponseEntity<SlideShowDto> addSlideshow(@RequestBody SlideshowRequest slideshowRequest) {
        return ResponseEntity.ok(slideShowService.createSlideShow(slideshowRequest));
    }

    @DeleteMapping(path = "/deleteSlideshow/{id}")
    public ResponseEntity<SlideShowDto> deleteSlideshow(@PathVariable long id) {
        return ResponseEntity.ok(slideShowService.getSlideShow(id));
    }

    @GetMapping(path = "/slideshow/{id}/slideshowOrder")
    public ResponseEntity<List<SlideDto>> slideshowOrder(@PathVariable long id) {
        return ResponseEntity.ok(slideShowService.getImagesInOrder(id));
    }

    @PostMapping(path = "/slideshow/{id}/proof-of-play/{imageId}")
    public void proofOfPlay(@PathVariable long id, @PathVariable long imageId) {
        slideShowService.recordOfPlay(id, imageId);
    }
}
