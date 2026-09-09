package com.sunuhelp.auth.exception;

import com.sunuhelp.auth.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Refresh token introuvable, expire ou deja revoque. */
public class InvalidRefreshTokenException extends BusinessException {
    public InvalidRefreshTokenException() {
        super(MessageKeys.REFRESH_TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
    }
}
