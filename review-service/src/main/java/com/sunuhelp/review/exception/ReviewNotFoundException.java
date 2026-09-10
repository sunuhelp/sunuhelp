package com.sunuhelp.review.exception;

import com.sunuhelp.review.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends BusinessException {
    public ReviewNotFoundException() {
        super(MessageKeys.REVIEW_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
