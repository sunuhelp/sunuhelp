package com.sunuhelp.category.exception;

import com.sunuhelp.category.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Un slug identique existe deja sous le meme parent (UNIQUE(parent_id, slug)). */
public class DuplicateSlugException extends BusinessException {
    public DuplicateSlugException() {
        super(MessageKeys.CATEGORY_DUPLICATE_SLUG, HttpStatus.CONFLICT);
    }
}
