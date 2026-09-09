package com.sunuhelp.category.exception;

import com.sunuhelp.category.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Suppression refusee tant que des sous-categories actives dependent de celle-ci. */
public class CategoryHasChildrenException extends BusinessException {
    public CategoryHasChildrenException() {
        super(MessageKeys.CATEGORY_HAS_CHILDREN, HttpStatus.CONFLICT);
    }
}
