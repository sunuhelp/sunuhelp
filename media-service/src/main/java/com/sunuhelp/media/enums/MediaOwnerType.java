package com.sunuhelp.media.enums;

/**
 * Type polymorphe du proprietaire d'un fichier - une seule table pour
 * tous les usages, plutot que 5 tables identiques dans leur structure.
 */
public enum MediaOwnerType {
    ENTITY_LOGO,
    ENTITY_PHOTO,
    OFFER_PHOTO,
    VERIFICATION_DOCUMENT,
    USER_AVATAR
}
