package com.sunuhelp.auth.exception;

import com.sunuhelp.auth.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Connexion refusee, compte suspendu par un Administrateur. */
public class AccountSuspendedException extends BusinessException {
    public AccountSuspendedException() {
        super(MessageKeys.ACCOUNT_SUSPENDED, HttpStatus.FORBIDDEN);
    }
}
