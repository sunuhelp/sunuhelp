package com.sunuhelp.user.exception;

import com.sunuhelp.user.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ProfileNotFoundException extends BusinessException {
    public ProfileNotFoundException() {
        super(MessageKeys.PROFILE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
