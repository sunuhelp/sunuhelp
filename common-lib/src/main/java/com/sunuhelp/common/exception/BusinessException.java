package com.sunuhelp.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception mere pour toute erreur metier. Transporte une CLE i18n
 * (jamais un texte deja traduit) et le code HTTP a renvoyer, pour que
 * GlobalExceptionHandler reste generique dans chaque service - pas besoin
 * d'un if/else par type d'exception.
 */
public abstract class BusinessException extends RuntimeException {

    private final String messageKey;
    private final transient Object[] args;
    private final HttpStatus httpStatus;

    protected BusinessException(String messageKey, HttpStatus httpStatus, Object... args) {
        this.messageKey = messageKey;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
