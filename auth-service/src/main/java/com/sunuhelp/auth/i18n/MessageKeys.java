package com.sunuhelp.auth.i18n;

/**
 * Cles de traduction utilisees par auth-service. Centralisees ici pour
 * qu'une faute de frappe soit detectee a la compilation plutot qu'a
 * l'execution, et pour eviter tout texte en dur dans le reste du code.
 */
public final class MessageKeys {

    // Otp
    /** Code saisi invalide (ne correspond a aucun hash stocke). */
    public static final String OTP_INVALID = "auth.otp.invalid";
    /** Code trouve mais date d'expiration depassee. */
    public static final String OTP_EXPIRED = "auth.otp.expired";
    /** Compteur de tentatives atteint, code bloque. */
    public static final String OTP_MAX_ATTEMPTS_REACHED = "auth.otp.max-attempts-reached";

    // Compte
    /** Creation refusee, le numero est deja utilise. */
    public static final String ACCOUNT_ALREADY_EXISTS = "auth.account.already-exists";
    /** Aucun compte pour ce numero. */
    public static final String ACCOUNT_NOT_FOUND = "auth.account.not-found";
    /** Compte existant mais jamais valide par OTP. */
    public static final String ACCOUNT_NOT_VERIFIED = "auth.account.not-verified";
    /** Compte bloque par un Administrateur. */
    public static final String ACCOUNT_SUSPENDED = "auth.account.suspended";
    /** Echec de connexion, identifiants incorrects. */
    public static final String ACCOUNT_INVALID_CREDENTIALS = "auth.account.invalid-credentials";

    // Validation des champs de requete
    public static final String VALIDATION_PHONE_REQUIRED = "validation.phone.required";
    public static final String VALIDATION_PHONE_INVALID_FORMAT = "validation.phone.invalid-format";
    public static final String VALIDATION_PASSWORD_REQUIRED = "validation.password.required";
    public static final String VALIDATION_PASSWORD_TOO_SHORT = "validation.password.too-short";

    // Empeche l'instanciation - classe utilitaire de constantes uniquement
    // Erreurs techniques
    /** Filet de securite pour toute exception non prevue. */
    public static final String ERROR_UNEXPECTED = "error.unexpected";

    public static final String REFRESH_TOKEN_INVALID = "auth.refresh-token.invalid";

    private MessageKeys() {
    }
}
