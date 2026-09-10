package com.sunuhelp.review.exception;

import com.sunuhelp.review.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Le compte a deja laisse un avis sur cette fiche - contrainte UNIQUE(entity_id, reviewer_account_id). */
public class ReviewAlreadyExistsException extends BusinessException {
    public ReviewAlreadyExistsException() {
        super(MessageKeys.REVIEW_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
}
