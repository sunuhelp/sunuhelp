package com.sunuhelp.media.exception;

import com.sunuhelp.media.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class FileTypeNotAllowedException extends BusinessException {
    public FileTypeNotAllowedException() {
        super(MessageKeys.FILE_TYPE_NOT_ALLOWED, HttpStatus.BAD_REQUEST);
    }
}
