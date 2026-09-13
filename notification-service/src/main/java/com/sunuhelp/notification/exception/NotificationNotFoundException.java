package com.sunuhelp.notification.exception;

import com.sunuhelp.notification.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends BusinessException {
    public NotificationNotFoundException() {
        super(MessageKeys.NOTIFICATION_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
