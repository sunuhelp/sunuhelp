package com.sunuhelp.geo.repository;

import com.sunuhelp.geo.entity.GeocodingCacheEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeocodingCacheRepository extends JpaRepository<GeocodingCacheEntry, java.util.UUID> {
    Optional<GeocodingCacheEntry> findByAddressQuery(String addressQuery);
}
