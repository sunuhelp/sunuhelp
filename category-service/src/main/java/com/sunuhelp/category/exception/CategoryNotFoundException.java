package com.sunuhelp.category.exception;

import com.sunuhelp.category.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Categorie introuvable a partir de son id. */
public class CategoryNotFoundException extends BusinessException {
    public CategoryNotFoundException() {
        super(MessageKeys.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
