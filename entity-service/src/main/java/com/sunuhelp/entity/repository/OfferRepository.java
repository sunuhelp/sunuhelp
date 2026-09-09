package com.sunuhelp.entity.repository;

import com.sunuhelp.entity.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findByServicePointIdAndActiveTrue(UUID servicePointId);
}
