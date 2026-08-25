package com.sunuhelp.auth.enums;

/**
 * Cycle de vie explicite du compte. Permet a l'Administrateur de suspendre
 * un compte abusif de facon reversible, sans le supprimer.
 */
public enum AccountStatus {
    /** Cree mais pas encore valide par OTP. */
    PENDING_VERIFICATION,
    /** Verifie et pleinement utilisable. */
    ACTIVE,
    /** Bloque temporairement par un Administrateur. */
    SUSPENDED,
    /** Desactive, generalement a la demande du titulaire. */
    DEACTIVATED
}
