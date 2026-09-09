package com.sunuhelp.entity.exception;

import com.sunuhelp.entity.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Fiche introuvable a partir de son id. */
public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException() {
        super(MessageKeys.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
