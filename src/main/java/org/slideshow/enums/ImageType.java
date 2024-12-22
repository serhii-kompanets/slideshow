package org.slideshow.enums;

import org.slideshow.exceptions.MimeTypeNotSupportedException;

import java.util.Arrays;

public enum ImageType {
    JPEG("image/jpeg"),
    PNG("image/png"),
    WEBP("image/webp");

    private final String mimeType;

    ImageType(String mimeType) {
        this.mimeType = mimeType;
    }

    public static ImageType getMimeType(String mimeType) {
        return Arrays.stream(ImageType.values())
                .filter(imageType -> imageType.mimeType.equals(mimeType))
                .findFirst()
                .orElseThrow(MimeTypeNotSupportedException::new);
    }
}
