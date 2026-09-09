package com.sunuhelp.category.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "category_translations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryTranslation extends BaseEntity {

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(nullable = false)
    private String locale;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    public static CategoryTranslation of(UUID categoryId, String locale, String name, String description) {
        CategoryTranslation translation = new CategoryTranslation();
        translation.categoryId = categoryId;
        translation.locale = locale;
        translation.name = name;
        translation.description = description;
        return translation;
    }

    public boolean isDefaultLocale() {
        return "fr".equals(locale);
    }
}
