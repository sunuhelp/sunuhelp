package com.sunuhelp.user.exception;

import com.sunuhelp.user.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class FavoriteNotFoundException extends BusinessException {
    public FavoriteNotFoundException() {
        super(MessageKeys.FAVORITE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
