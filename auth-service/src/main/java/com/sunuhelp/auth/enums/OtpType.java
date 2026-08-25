package com.sunuhelp.auth.enums;

/**
 * Distingue les differents usages d'un meme mecanisme OTP, pour eviter
 * de creer une table separee par usage (inscription, connexion,
 * reinitialisation de mot de passe, changement de numero).
 */
public enum OtpType {
    REGISTRATION,
    LOGIN,
    PASSWORD_RESET,
    PHONE_CHANGE
}
