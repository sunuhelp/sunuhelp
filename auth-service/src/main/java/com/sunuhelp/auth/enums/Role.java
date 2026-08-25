package com.sunuhelp.auth.enums;

/**
 * Role de base d'un compte. "Proprietaire d'entite" n'est jamais un role
 * ici : c'est un fait constate par entity-service (owner_account_id),
 * pas une valeur stockee dans auth-service - decision prise lors de la
 * modelisation pour permettre a un meme compte d'etre Usager ET
 * proprietaire d'une ou plusieurs entites.
 */
public enum Role {
    USER,
    ADMIN
}
