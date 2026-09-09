package com.sunuhelp.category.controller;

import com.sunuhelp.category.dto.request.CreateCategoryRequest;
import com.sunuhelp.category.dto.request.UpdateCategoryRequest;
import com.sunuhelp.category.dto.response.CategoryResponse;
import com.sunuhelp.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Liste les categories racines")
    public ResponseEntity<List<CategoryResponse>> findRoots(Locale locale) {
        return ResponseEntity.ok(categoryService.findRoots(locale.getLanguage()));
    }

    @GetMapping("/{id}/children")
    @Operation(summary = "Liste les sous-categories d'une categorie")
    public ResponseEntity<List<CategoryResponse>> findChildren(@PathVariable UUID id, Locale locale) {
        return ResponseEntity.ok(categoryService.findChildren(id, locale.getLanguage()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detail d'une categorie, traduction resolue")
    public ResponseEntity<CategoryResponse> findById(@PathVariable UUID id, Locale locale) {
        return ResponseEntity.ok(categoryService.findById(id, locale.getLanguage()));
    }

    @PostMapping
    @Operation(summary = "Cree une categorie ou sous-categorie (Admin uniquement)")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request,
                                                     Authentication authentication, Locale locale) {
        UUID adminId = (UUID) authentication.getPrincipal();
        CategoryResponse response = categoryService.create(request, adminId, locale.getLanguage());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifie une categorie - jamais son slug (Admin uniquement)")
    public ResponseEntity<CategoryResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody UpdateCategoryRequest request,
                                                     Authentication authentication, Locale locale) {
        UUID adminId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(categoryService.update(id, request, adminId, locale.getLanguage()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime - refuse si des sous-categories actives existent (Admin uniquement)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{sourceId}/merge/{targetId}")
    @Operation(summary = "Fusionne sourceId dans targetId (Admin uniquement)")
    public ResponseEntity<Void> merge(@PathVariable UUID sourceId, @PathVariable UUID targetId,
                                       Authentication authentication) {
        UUID adminId = (UUID) authentication.getPrincipal();
        categoryService.merge(sourceId, targetId, adminId);
        return ResponseEntity.noContent().build();
    }
}
