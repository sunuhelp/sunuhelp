package com.sunuhelp.auth.exception;

import com.sunuhelp.auth.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Numero ou mot de passe incorrect a la connexion. */
public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super(MessageKeys.ACCOUNT_INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }
}
