package com.sunuhelp.entity.exception;

import com.sunuhelp.entity.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OfferNotFoundException extends BusinessException {
    public OfferNotFoundException() {
        super(MessageKeys.OFFER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
