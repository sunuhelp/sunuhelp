package com.sunuhelp.entity.enums;

/**
 * Type de personne titulaire de l'Entite. Determine dynamiquement les
 * pieces justificatives demandees (CNI pour une personne physique,
 * RCCM/NINEA pour une personne morale).
 */
public enum PersonType {
    INDIVIDUAL,
    LEGAL_ENTITY
}
