package com.sunuhelp.entity.exception;

import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Adresse manquante pour un point physique, ou zone de couverture manquante pour un point en ligne. */
public class InvalidServicePointException extends BusinessException {
    public InvalidServicePointException(String messageKey) {
        super(messageKey, HttpStatus.BAD_REQUEST);
    }
}
