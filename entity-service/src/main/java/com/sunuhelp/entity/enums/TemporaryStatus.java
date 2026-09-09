package com.sunuhelp.entity.enums;

/**
 * Statut temporaire d'un point de service, plus robuste qu'un simple
 * booleen "ferme" : distingue une fermeture totale d'une disponibilite
 * partielle (ex. pharmacie en rupture de stock partielle).
 */
public enum TemporaryStatus {
    NONE,
    TEMPORARILY_CLOSED,
    PARTIALLY_AVAILABLE
}
