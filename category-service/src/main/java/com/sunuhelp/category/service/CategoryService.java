package com.sunuhelp.category.service;

import com.sunuhelp.category.dto.request.CreateCategoryRequest;
import com.sunuhelp.category.dto.request.UpdateCategoryRequest;
import com.sunuhelp.category.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse create(CreateCategoryRequest request, UUID adminAccountId, String locale);

    List<CategoryResponse> findRoots(String locale);

    List<CategoryResponse> findChildren(UUID parentId, String locale);

    CategoryResponse findById(UUID id, String locale);

    CategoryResponse update(UUID id, UpdateCategoryRequest request, UUID adminAccountId, String locale);

    /** Refuse si des sous-categories actives dependent de id. */
    void delete(UUID id);

    /** Fusionne sourceId dans targetId, publie l'evenement pour entity-service. */
    void merge(UUID sourceId, UUID targetId, UUID adminAccountId);
}
