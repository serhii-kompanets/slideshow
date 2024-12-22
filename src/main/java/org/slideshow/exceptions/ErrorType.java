package org.slideshow.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    /** System can't work with passed content-type/content-encoding. */
    UNSUPPORTED_MEDIA_TYPE(202, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE", false),

    /** General internal server error. Something went wrong. */
    INTERNAL_SERVER_ERROR(180, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", false);

    private final int applicationCode;

    private final HttpStatus httpStatusCode;

    private final String messageTemplate;

    private final boolean templateHasParameter;

    private ErrorType(
            final int applicationCode,
            final HttpStatus httpStatusCode,
            final String messageTemplate,
            final boolean templateHasParameter
    ) {
        this.applicationCode      = applicationCode;
        this.httpStatusCode       = httpStatusCode;
        this.messageTemplate      = messageTemplate;
        this.templateHasParameter = templateHasParameter;
    }

    public int applicationCode() {
        return applicationCode;
    }

    public HttpStatus httpStatusCode() {
        return httpStatusCode;
    }

    public String message() {
        return messageTemplate;
    }

    public String message(final String arg) {
        return templateHasParameter
                ? String.format(messageTemplate, arg)
                : messageTemplate;
    }

    public static ErrorType valueOf(final int applicationCode) {
        ErrorType result = null;
        for (ErrorType e : values()) {
            if (applicationCode == e.applicationCode) {
                result = e;
                break;
            }
        }
        if (result == null) {
            result = INTERNAL_SERVER_ERROR;
        }
        return result;
    }
}
