package com.sunuhelp.auth.service;

import com.sunuhelp.auth.entity.Account;
import com.sunuhelp.auth.enums.OtpType;

public interface OtpService {

    /** Genere un code, le sauvegarde hache, et publie l'evenement d'envoi SMS. */
    void generateAndSend(Account account, OtpType otpType);

    /** Verifie le code saisi - leve une exception metier si invalide/expire/trop de tentatives. */
    void verify(Account account, OtpType otpType, String rawCode);
}
