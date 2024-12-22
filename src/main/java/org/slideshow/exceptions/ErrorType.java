package org.slideshow.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum ErrorType {
    IMAGE_NOT_FOUND(101, "Image not found"),
    SLIDESHOW_NOT_FOUND(102, "Slideshow not found"),

    /** System can't work with passed content-type/content-encoding. */
    UNSUPPORTED_MEDIA_TYPE(202, "UNSUPPORTED_MEDIA_TYPE"),
    UPLOAD_FILE_IS_BROKEN(203, "Upload file is broken"),
    IMAGE_MAX_SIZE_IS_TOO_LARGE(204, "Image max size is too large"),

    /** General internal server error. Something went wrong. */
    INTERNAL_SERVER_ERROR(180, "INTERNAL_SERVER_ERROR");

    private final int applicationCode;
    private final String message;

    @JsonCreator
    ErrorType(
            @JsonProperty("applicationCode") final int applicationCode,
            @JsonProperty("message") final String message
    ) {
        this.applicationCode = applicationCode;
        this.message = message;
    }

    @JsonGetter
    public int getApplicationCode() {
        return applicationCode;
    }

    @JsonGetter
    public String getMessage() {
        return message;
    }
}
