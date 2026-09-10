package com.sunuhelp.user.exception;

import com.sunuhelp.user.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Ni queryText ni categoryId renseignes - au moins un des deux est obligatoire. */
public class SearchCriteriaRequiredException extends BusinessException {
    public SearchCriteriaRequiredException() {
        super(MessageKeys.SEARCH_HISTORY_CRITERIA_REQUIRED, HttpStatus.BAD_REQUEST);
    }
}
