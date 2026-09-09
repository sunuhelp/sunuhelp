package com.sunuhelp.entity.exception;

import com.sunuhelp.entity.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DocumentNotFoundException extends BusinessException {
    public DocumentNotFoundException() {
        super(MessageKeys.DOCUMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
