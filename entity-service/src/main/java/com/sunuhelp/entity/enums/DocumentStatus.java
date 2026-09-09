package com.sunuhelp.entity.enums;

/**
 * Statut par document, pas juste par fiche - permet a l'Administrateur
 * de valider/rejeter chaque piece individuellement (ex. photo recto
 * illisible mais verso ok).
 */
public enum DocumentStatus {
    PENDING,
    APPROVED,
    REJECTED
}
