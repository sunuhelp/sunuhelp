package com.sunuhelp.geo.service.impl;

import com.sunuhelp.geo.client.NominatimClient;
import com.sunuhelp.geo.client.NominatimRateLimiter;
import com.sunuhelp.geo.client.NominatimResult;
import com.sunuhelp.geo.dto.response.GeocodeResponse;
import com.sunuhelp.geo.entity.GeocodingCacheEntry;
import com.sunuhelp.geo.exception.GeocodingFailedException;
import com.sunuhelp.geo.repository.GeocodingCacheRepository;
import com.sunuhelp.geo.service.GeocodingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    private static final String PROVIDER = "nominatim";

    private final GeocodingCacheRepository cacheRepository;
    private final NominatimClient nominatimClient;
    private final NominatimRateLimiter rateLimiter;

    public GeocodingServiceImpl(GeocodingCacheRepository cacheRepository,
                                 NominatimClient nominatimClient,
                                 NominatimRateLimiter rateLimiter) {
        this.cacheRepository = cacheRepository;
        this.nominatimClient = nominatimClient;
        this.rateLimiter = rateLimiter;
    }

    @Override
    @Transactional
    public GeocodeResponse geocode(String address) {
        String normalized = normalize(address);

        Optional<GeocodingCacheEntry> cached = cacheRepository.findByAddressQuery(normalized);
        if (cached.isPresent() && !cached.get().isExpired()) {
            GeocodingCacheEntry entry = cached.get();
            return GeocodeResponse.builder()
                    .latitude(entry.getLatitude())
                    .longitude(entry.getLongitude())
                    .confidenceScore(entry.getConfidenceScore())
                    .fromCache(true)
                    .build();
        }

        rateLimiter.waitForNextSlot();
        List<NominatimResult> results = nominatimClient.search(address);

        if (results == null || results.isEmpty()) {
            throw new GeocodingFailedException();
        }

        NominatimResult result = results.get(0);
        BigDecimal latitude = new BigDecimal(result.getLat());
        BigDecimal longitude = new BigDecimal(result.getLon());
        BigDecimal confidence = result.getPlaceRank() != null
                ? BigDecimal.valueOf(Math.min(1.0, result.getPlaceRank() / 30.0))
                : BigDecimal.ZERO;

        GeocodingCacheEntry entry = GeocodingCacheEntry.create(normalized, latitude, longitude, PROVIDER, confidence);

        // Seuil de fiabilite : seuls les resultats >= 0.85 sont mis en cache,
        // pour ne jamais propager une adresse ambigue a toutes les futures
        // Entites qui saisiraient une adresse similaire (decide en modelisation).
        if (entry.isReliable()) {
            cacheRepository.save(entry);
        }

        return GeocodeResponse.builder()
                .latitude(latitude)
                .longitude(longitude)
                .confidenceScore(confidence)
                .fromCache(false)
                .build();
    }

    /** Normalise l'adresse (minuscules, espaces reduits) pour ameliorer les correspondances de cache. */
    private String normalize(String address) {
        return address.trim().toLowerCase().replaceAll("\\s+", " ");
    }
}
