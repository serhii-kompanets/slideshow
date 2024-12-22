package org.slideshow.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slideshow.dtos.ErrorResponse;
import org.slideshow.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class ErrorControllerAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ImageNotFoundException ex) { // 404
        LOGGER.warn(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorType.IMAGE_NOT_FOUND));
    }

    @ExceptionHandler(SlideshowNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(SlideshowNotFoundException ex) { // 404
        LOGGER.warn(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorType.IMAGE_NOT_FOUND));
    }

    @ExceptionHandler(MimeTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MimeTypeNotSupportedException ex) { // 400
        LOGGER.warn(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorType.UNSUPPORTED_MEDIA_TYPE));
    }

    @ExceptionHandler(UploadFileBrokenException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(UploadFileBrokenException ex) { // 400
        LOGGER.warn(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorType.UPLOAD_FILE_IS_BROKEN));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MaxUploadSizeExceededException ex) { // 400
        LOGGER.warn(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorType.UNSUPPORTED_MEDIA_TYPE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleValidationException(Exception ex) { // 500
        LOGGER.warn(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorType.INTERNAL_SERVER_ERROR));
    }
}
