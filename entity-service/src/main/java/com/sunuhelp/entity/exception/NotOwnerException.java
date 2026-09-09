package com.sunuhelp.entity.exception;

import com.sunuhelp.entity.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Le compte connecte n'est pas proprietaire de la fiche qu'il tente de modifier. */
public class NotOwnerException extends BusinessException {
    public NotOwnerException() {
        super(MessageKeys.ENTITY_NOT_OWNER, HttpStatus.FORBIDDEN);
    }
}
