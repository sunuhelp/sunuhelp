package com.sunuhelp.entity.i18n;

/**
 * Cles de traduction utilisees par entity-service. Centralisees pour
 * detecter une faute de frappe a la compilation, jamais a l'execution.
 */
public final class MessageKeys {

    // Entite
    public static final String ENTITY_NOT_FOUND = "entity.not-found";
    public static final String ENTITY_NOT_OWNER = "entity.not-owner";
    public static final String ENTITY_EMAIL_REQUIRED = "entity.email-required";

    // Point de service
    public static final String SERVICE_POINT_NOT_FOUND = "service-point.not-found";
    public static final String SERVICE_POINT_ADDRESS_REQUIRED = "service-point.address-required";
    public static final String SERVICE_POINT_COVERAGE_ZONE_REQUIRED = "service-point.coverage-zone-required";

    // Offre
    public static final String OFFER_NOT_FOUND = "offer.not-found";

    // Document
    public static final String DOCUMENT_NOT_FOUND = "document.not-found";

    // Signalement
    public static final String REPORT_NOT_FOUND = "report.not-found";

    // Validation
    public static final String VALIDATION_NAME_REQUIRED = "validation.name.required";
    public static final String VALIDATION_CATEGORY_REQUIRED = "validation.category.required";

    public static final String ERROR_UNEXPECTED = "error.unexpected";

    private MessageKeys() {
    }
}
