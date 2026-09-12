package com.sunuhelp.media.exception;

import com.sunuhelp.media.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Tentative d'acces a un fichier prive (document justificatif) sans en etre le proprietaire ni Admin. */
public class FileAccessDeniedException extends BusinessException {
    public FileAccessDeniedException() {
        super(MessageKeys.FILE_ACCESS_DENIED, HttpStatus.FORBIDDEN);
    }
}
