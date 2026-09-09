package com.sunuhelp.entity.enums;

/**
 * Niveau de confiance affiche en tete de fiche. Passe a VERIFIED
 * uniquement apres approbation d'au moins un document justificatif par
 * un Administrateur - jamais automatique.
 */
public enum TrustLevel {
    UNVERIFIED,
    VERIFIED
}
