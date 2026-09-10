package com.sunuhelp.review.exception;

import com.sunuhelp.review.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidRatingException extends BusinessException {
    public InvalidRatingException() {
        super(MessageKeys.REVIEW_INVALID_RATING, HttpStatus.BAD_REQUEST);
    }
}
