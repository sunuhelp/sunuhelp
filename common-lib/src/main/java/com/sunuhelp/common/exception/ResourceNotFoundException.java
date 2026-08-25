package com.sunuhelp.common.exception;

import org.springframework.http.HttpStatus;

/**
 * A lever quand une ressource demandee (compte, entite...) n'existe pas.
 * Toujours traduite en HTTP 404.
 */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String messageKey, Object... args) {
        super(messageKey, HttpStatus.NOT_FOUND, args);
    }
}
