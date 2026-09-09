package com.sunuhelp.entity.enums;

/**
 * Resultat du geocodage d'une adresse par geo-service. FAILED ne bloque
 * jamais la publication de la fiche, mais l'exclut de la recherche par
 * proximite tant que l'Entite n'a pas corrige ou place un point manuellement.
 */
public enum GeocodingStatus {
    PENDING,
    SUCCESS,
    FAILED
}
