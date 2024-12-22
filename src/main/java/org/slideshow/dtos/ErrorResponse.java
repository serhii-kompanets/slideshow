package org.slideshow.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slideshow.exceptions.ErrorType;

public class ErrorResponse {
    private final ErrorType errorType;

    @JsonCreator
    public ErrorResponse(@JsonProperty("errorType") ErrorType error) {
        this.errorType = error;
    }

    @JsonGetter
    public ErrorType getErrorType() {
        return errorType;
    }
}
