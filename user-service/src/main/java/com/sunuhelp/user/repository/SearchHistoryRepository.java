package com.sunuhelp.user.repository;

import com.sunuhelp.user.entity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, UUID> {
    Page<SearchHistory> findByAccountIdOrderBySearchedAtDesc(UUID accountId, Pageable pageable);
}
