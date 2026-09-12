package com.sunuhelp.media.exception;

import com.sunuhelp.media.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Nommee ainsi (pas FileNotFoundException) pour eviter la collision avec java.io.FileNotFoundException. */
public class FileNotFoundBusinessException extends BusinessException {
    public FileNotFoundBusinessException() {
        super(MessageKeys.FILE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
