package com.sunuhelp.entity.exception;

import com.sunuhelp.entity.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ServicePointNotFoundException extends BusinessException {
    public ServicePointNotFoundException() {
        super(MessageKeys.SERVICE_POINT_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
