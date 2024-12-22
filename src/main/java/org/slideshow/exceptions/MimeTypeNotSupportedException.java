package org.slideshow.exceptions;

public class MimeTypeNotSupportedException extends ApplicationException {
    public MimeTypeNotSupportedException() {
    }

    public MimeTypeNotSupportedException(String message) {
        super(message);
    }
}
