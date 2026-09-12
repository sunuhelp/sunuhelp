package com.sunuhelp.media.enums;

/**
 * Cycle de vie du traitement d'un fichier uploade. SCANNING/REJECTED
 * existent pour l'analyse de securite (contenu malveillant), distincts
 * d'un simple echec technique (FAILED).
 */
public enum ProcessingStatus {
    PENDING,
    SCANNING,
    READY,
    REJECTED,
    FAILED
}
