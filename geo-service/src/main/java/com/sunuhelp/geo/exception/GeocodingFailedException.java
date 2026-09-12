package com.sunuhelp.geo.exception;

import com.sunuhelp.geo.i18n.MessageKeys;
import com.sunuhelp.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** L'adresse n'a pas pu etre geocodee - Nominatim n'a renvoye aucun resultat exploitable. */
public class GeocodingFailedException extends BusinessException {
    public GeocodingFailedException() {
        super(MessageKeys.GEOCODING_FAILED, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
