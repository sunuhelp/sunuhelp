package com.sunuhelp.user.exception;

import com.sunuhelp.user.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class FavoriteAlreadyExistsException extends BusinessException {
    public FavoriteAlreadyExistsException() {
        super(MessageKeys.FAVORITE_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
}
