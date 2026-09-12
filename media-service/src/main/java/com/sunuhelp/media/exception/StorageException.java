package com.sunuhelp.media.exception;

import com.sunuhelp.media.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class StorageException extends BusinessException {
    public StorageException() {
        super(MessageKeys.STORAGE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
