package com.sunuhelp.auth.exception;

import com.sunuhelp.auth.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Connexion refusee, le compte existe mais n'a jamais valide son OTP. */
public class AccountNotVerifiedException extends BusinessException {
    public AccountNotVerifiedException() {
        super(MessageKeys.ACCOUNT_NOT_VERIFIED, HttpStatus.FORBIDDEN);
    }
}
