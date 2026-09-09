package com.sunuhelp.entity.repository;

import com.sunuhelp.entity.entity.OfferTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfferTranslationRepository extends JpaRepository<OfferTranslation, UUID> {
    List<OfferTranslation> findByOfferId(UUID offerId);
}
