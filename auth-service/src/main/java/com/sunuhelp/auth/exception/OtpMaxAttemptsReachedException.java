package com.sunuhelp.auth.exception;

import com.sunuhelp.auth.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Trop de tentatives de validation pour un meme code - protection anti
 * brute-force. 429 plutot que 400 : signale explicitement au client qu'il
 * doit ralentir, pas juste corriger sa saisie.
 */
public class OtpMaxAttemptsReachedException extends BusinessException {
    public OtpMaxAttemptsReachedException() {
        super(MessageKeys.OTP_MAX_ATTEMPTS_REACHED, HttpStatus.TOO_MANY_REQUESTS);
    }
}
