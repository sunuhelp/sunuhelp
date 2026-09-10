package com.sunuhelp.review.exception;

import com.sunuhelp.review.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReportNotFoundException extends BusinessException {
    public ReportNotFoundException() {
        super(MessageKeys.REPORT_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
