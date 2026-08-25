package com.sunuhelp.auth.exception;

import com.sunuhelp.auth.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Code trouve mais la fenetre de validite (5 min) est depassee. */
public class OtpExpiredException extends BusinessException {
    public OtpExpiredException() {
        super(MessageKeys.OTP_EXPIRED, HttpStatus.BAD_REQUEST);
    }
}
