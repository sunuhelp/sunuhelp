package com.sunuhelp.user.controller;

import com.sunuhelp.user.dto.request.CreateSearchHistoryRequest;
import com.sunuhelp.user.dto.response.SearchHistoryResponse;
import com.sunuhelp.user.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/search-history")
@Tag(name = "Historique de recherche")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    public SearchHistoryController(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    @PostMapping
    @Operation(summary = "Enregistre une recherche - ignore silencieusement si le suivi est desactive")
    public ResponseEntity<Void> record(@Valid @RequestBody CreateSearchHistoryRequest request,
                                         Authentication authentication) {
        UUID accountId = (UUID) authentication.getPrincipal();
        searchHistoryService.record(accountId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Liste son historique de recherche")
    public ResponseEntity<Page<SearchHistoryResponse>> findOwn(Authentication authentication, Pageable pageable) {
        UUID accountId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(searchHistoryService.findByAccount(accountId, pageable));
    }
}
