package com.sunuhelp.category.exception;

import com.sunuhelp.category.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Tentative de creer un 3e niveau - la limite (2 niveaux) est imposee en code, pas en base. */
public class MaxCategoryDepthExceededException extends BusinessException {
    public MaxCategoryDepthExceededException() {
        super(MessageKeys.CATEGORY_MAX_DEPTH_EXCEEDED, HttpStatus.BAD_REQUEST);
    }
}
