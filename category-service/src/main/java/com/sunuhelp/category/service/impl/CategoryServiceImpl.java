package com.sunuhelp.category.service.impl;

import com.sunuhelp.category.dto.request.CreateCategoryRequest;
import com.sunuhelp.category.dto.request.TranslationRequest;
import com.sunuhelp.category.dto.request.UpdateCategoryRequest;
import com.sunuhelp.category.dto.response.CategoryResponse;
import com.sunuhelp.category.entity.Category;
import com.sunuhelp.category.entity.CategoryTranslation;
import com.sunuhelp.category.event.CategoryDeletedEvent;
import com.sunuhelp.category.event.CategoryMergedEvent;
import com.sunuhelp.category.event.EventProducer;
import com.sunuhelp.category.exception.CategoryHasChildrenException;
import com.sunuhelp.category.exception.CategoryNotFoundException;
import com.sunuhelp.category.exception.DuplicateSlugException;
import com.sunuhelp.category.exception.MaxCategoryDepthExceededException;
import com.sunuhelp.category.mapper.CategoryMapper;
import com.sunuhelp.category.mapper.CategoryTranslationResolver;
import com.sunuhelp.category.repository.CategoryRepository;
import com.sunuhelp.category.repository.CategoryTranslationRepository;
import com.sunuhelp.category.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final int MAX_DEPTH = 1; // 2 niveaux au total : 0 (racine) et 1 (sous-categorie)

    private final CategoryRepository categoryRepository;
    private final CategoryTranslationRepository translationRepository;
    private final CategoryTranslationResolver translationResolver;
    private final CategoryMapper categoryMapper;
    private final EventProducer eventProducer;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                                CategoryTranslationRepository translationRepository,
                                CategoryTranslationResolver translationResolver,
                                CategoryMapper categoryMapper,
                                EventProducer eventProducer) {
        this.categoryRepository = categoryRepository;
        this.translationRepository = translationRepository;
        this.translationResolver = translationResolver;
        this.categoryMapper = categoryMapper;
        this.eventProducer = eventProducer;
    }

    @Override
    @Transactional
    public CategoryResponse create(CreateCategoryRequest request, UUID adminAccountId, String locale) {
        Category category;

        if (request.getParentId() == null) {
            if (categoryRepository.existsByParentIdAndSlugAndActiveTrue(null, request.getSlug())) {
                throw new DuplicateSlugException();
            }
            category = Category.createRoot(request.getSlug(), request.getIcon(),
                    request.getDisplayOrder(), request.isRequiresValidation(), adminAccountId);
        } else {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(CategoryNotFoundException::new);

            if (parent.getDepth() >= MAX_DEPTH) {
                throw new MaxCategoryDepthExceededException();
            }
            if (categoryRepository.existsByParentIdAndSlugAndActiveTrue(parent.getId(), request.getSlug())) {
                throw new DuplicateSlugException();
            }
            category = Category.createChild(request.getSlug(), parent.getId(), parent.getDepth(),
                    request.getIcon(), request.getDisplayOrder(), request.isRequiresValidation(), adminAccountId);
        }

        categoryRepository.save(category);

        for (TranslationRequest t : request.getTranslations()) {
            translationRepository.save(
                    CategoryTranslation.of(category.getId(), t.getLocale(), t.getName(), t.getDescription()));
        }

        return toResponse(category, locale);
    }

    @Override
    public List<CategoryResponse> findRoots(String locale) {
        return categoryRepository.findByParentIdIsNullAndActiveTrueOrderByDisplayOrder().stream()
                .map(c -> toResponse(c, locale))
                .toList();
    }

    @Override
    public List<CategoryResponse> findChildren(UUID parentId, String locale) {
        return categoryRepository.findByParentIdAndActiveTrueOrderByDisplayOrder(parentId).stream()
                .map(c -> toResponse(c, locale))
                .toList();
    }

    @Override
    public CategoryResponse findById(UUID id, String locale) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        return toResponse(category, locale);
    }

    @Override
    @Transactional
    public CategoryResponse update(UUID id, UpdateCategoryRequest request, UUID adminAccountId, String locale) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        // icon/displayOrder/requiresValidation modifies directement, jamais le slug (cf UpdateCategoryRequest)
        category.reorder(request.getDisplayOrder(), adminAccountId);
        categoryRepository.save(category);
        return toResponse(category, locale);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        if (categoryRepository.existsByParentIdAndActiveTrue(id)) {
            throw new CategoryHasChildrenException();
        }

        category.softDelete();
        categoryRepository.save(category);
        eventProducer.publish(CategoryDeletedEvent.of(id));
    }

    @Override
    @Transactional
    public void merge(UUID sourceId, UUID targetId, UUID adminAccountId) {
        Category source = categoryRepository.findById(sourceId).orElseThrow(CategoryNotFoundException::new);
        // Verifie aussi que targetId existe reellement avant de publier l'evenement.
        categoryRepository.findById(targetId).orElseThrow(CategoryNotFoundException::new);

        source.markMerged(adminAccountId);
        categoryRepository.save(source);

        eventProducer.publish(CategoryMergedEvent.of(sourceId, targetId));
    }

    private CategoryResponse toResponse(Category category, String locale) {
        CategoryTranslation translation = translationResolver.resolve(category.getId(), locale);
        return categoryMapper.toResponse(category, translation);
    }
}
