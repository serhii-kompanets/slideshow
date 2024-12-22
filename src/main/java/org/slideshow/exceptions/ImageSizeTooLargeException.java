package org.slideshow.exceptions;

//MaxUploadSizeExceededException
public class ImageSizeTooLargeException extends ApplicationException {
    public ImageSizeTooLargeException() {
    }

    public ImageSizeTooLargeException(String message) {
        super(message);
    }
}
