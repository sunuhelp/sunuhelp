package com.sunuhelp.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Enveloppe de reponse paginee, commune a tous les microservices.
 * Volontairement independante du type Page<T> de Spring Data : ne jamais
 * renvoyer un Page<T> directement au client (expose des details internes
 * de pagination non voulus, et couple l'API a une bibliotheque precise).
 */
@Getter
public class PageResponse<T> {

    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    private PageResponse(List<T> content, int pageNumber, int pageSize,
                          long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    /**
     * Construit une PageResponse a partir d'un Page<T> Spring Data,
     * point de passage unique entre la couche persistance et l'API.
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
