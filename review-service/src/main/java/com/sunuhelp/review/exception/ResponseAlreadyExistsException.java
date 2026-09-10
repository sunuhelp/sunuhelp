package com.sunuhelp.review.exception;

import com.sunuhelp.review.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Une reponse existe deja pour cet avis - contrainte UNIQUE(review_id), jamais modifiable. */
public class ResponseAlreadyExistsException extends BusinessException {
    public ResponseAlreadyExistsException() {
        super(MessageKeys.RESPONSE_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
}
