package com.sunuhelp.category.i18n;

/**
 * Cles de traduction utilisees par category-service. Centralisees pour
 * detecter une faute de frappe a la compilation, jamais a l'execution.
 */
public final class MessageKeys {

    public static final String CATEGORY_NOT_FOUND = "category.not-found";
    public static final String CATEGORY_DUPLICATE_SLUG = "category.duplicate-slug";
    public static final String CATEGORY_MAX_DEPTH_EXCEEDED = "category.max-depth-exceeded";
    public static final String CATEGORY_HAS_CHILDREN = "category.has-children";

    public static final String VALIDATION_SLUG_REQUIRED = "validation.slug.required";
    public static final String VALIDATION_NAME_REQUIRED = "validation.name.required";
    public static final String VALIDATION_LOCALE_REQUIRED = "validation.locale.required";

    public static final String ERROR_UNEXPECTED = "error.unexpected";

    private MessageKeys() {
    }
}
