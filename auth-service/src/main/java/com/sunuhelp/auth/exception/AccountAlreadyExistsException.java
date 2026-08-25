package com.sunuhelp.auth.exception;

import com.sunuhelp.auth.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Inscription refusee, le numero de telephone est deja utilise. */
public class AccountAlreadyExistsException extends BusinessException {
    public AccountAlreadyExistsException() {
        super(MessageKeys.ACCOUNT_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
}
