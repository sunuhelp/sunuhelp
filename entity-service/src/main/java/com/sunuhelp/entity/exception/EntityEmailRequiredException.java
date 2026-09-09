package com.sunuhelp.entity.exception;

import com.sunuhelp.entity.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Le compte n'a pas d'email renseigne - obligatoire pour creer une premiere fiche d'Entite. */
public class EntityEmailRequiredException extends BusinessException {
    public EntityEmailRequiredException() {
        super(MessageKeys.ENTITY_EMAIL_REQUIRED, HttpStatus.BAD_REQUEST);
    }
}
