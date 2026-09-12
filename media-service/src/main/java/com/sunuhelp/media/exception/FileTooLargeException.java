package com.sunuhelp.media.exception;

import com.sunuhelp.media.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class FileTooLargeException extends BusinessException {
    public FileTooLargeException() {
        super(MessageKeys.FILE_TOO_LARGE, HttpStatus.BAD_REQUEST);
    }
}
