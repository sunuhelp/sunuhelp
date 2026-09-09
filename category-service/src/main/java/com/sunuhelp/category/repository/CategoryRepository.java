package com.sunuhelp.category.repository;

import com.sunuhelp.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /** Categories racines, triees pour l'accueil. */
    List<Category> findByParentIdIsNullAndActiveTrueOrderByDisplayOrder();

    /** Sous-categories d'une categorie donnee. */
    List<Category> findByParentIdAndActiveTrueOrderByDisplayOrder(UUID parentId);

    /** Verifie l'unicite du slug sous un parent precis (racine incluse via parentId=null). */
    boolean existsByParentIdAndSlugAndActiveTrue(UUID parentId, String slug);

    /** Necessaire avant suppression - bloque si des enfants actifs existent. */
    boolean existsByParentIdAndActiveTrue(UUID parentId);
}
