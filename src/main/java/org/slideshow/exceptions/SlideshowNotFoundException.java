package org.slideshow.exceptions;

public class SlideshowNotFoundException extends ApplicationException {
    public SlideshowNotFoundException() {
    }

    public SlideshowNotFoundException(String message) {
        super(message);
    }
}
