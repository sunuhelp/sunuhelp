package com.sunuhelp.auth.exception;

import com.sunuhelp.auth.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Code OTP saisi qui ne correspond a aucun code valide en attente. */
public class InvalidOtpException extends BusinessException {
    public InvalidOtpException() {
        super(MessageKeys.OTP_INVALID, HttpStatus.BAD_REQUEST);
    }
}
