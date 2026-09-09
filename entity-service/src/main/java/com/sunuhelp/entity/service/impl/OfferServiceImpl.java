package com.sunuhelp.entity.service.impl;

import com.sunuhelp.entity.dto.request.CreateOfferRequest;
import com.sunuhelp.entity.dto.request.OfferTranslationRequest;
import com.sunuhelp.entity.dto.response.OfferResponse;
import com.sunuhelp.entity.entity.Offer;
import com.sunuhelp.entity.entity.OfferTranslation;
import com.sunuhelp.entity.entity.ServicePoint;
import com.sunuhelp.entity.exception.ServicePointNotFoundException;
import com.sunuhelp.entity.mapper.OfferMapper;
import com.sunuhelp.entity.mapper.OfferTranslationResolver;
import com.sunuhelp.entity.repository.OfferRepository;
import com.sunuhelp.entity.repository.OfferTranslationRepository;
import com.sunuhelp.entity.repository.ServicePointRepository;
import com.sunuhelp.entity.service.EntityOwnershipValidator;
import com.sunuhelp.entity.service.OfferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferTranslationRepository translationRepository;
    private final ServicePointRepository servicePointRepository;
    private final EntityOwnershipValidator ownershipValidator;
    private final OfferMapper offerMapper;
    private final OfferTranslationResolver translationResolver;

    public OfferServiceImpl(OfferRepository offerRepository,
                             OfferTranslationRepository translationRepository,
                             ServicePointRepository servicePointRepository,
                             EntityOwnershipValidator ownershipValidator,
                             OfferMapper offerMapper,
                             OfferTranslationResolver translationResolver) {
        this.offerRepository = offerRepository;
        this.translationRepository = translationRepository;
        this.servicePointRepository = servicePointRepository;
        this.ownershipValidator = ownershipValidator;
        this.offerMapper = offerMapper;
        this.translationResolver = translationResolver;
    }

    @Override
    @Transactional
    public OfferResponse create(UUID servicePointId, CreateOfferRequest request, UUID requesterAccountId) {
        ServicePoint point = servicePointRepository.findById(servicePointId)
                .orElseThrow(ServicePointNotFoundException::new);
        ownershipValidator.assertOwner(point.getEntityId(), requesterAccountId);

        Offer offer = Offer.create(servicePointId, request.getPrice(), request.getAvailability());
        offerRepository.save(offer);

        OfferTranslation firstTranslation = null;
        for (OfferTranslationRequest t : request.getTranslations()) {
            OfferTranslation saved = translationRepository.save(
                    OfferTranslation.of(offer.getId(), t.getLocale(), t.getTitle(), t.getDescription()));
            if (firstTranslation == null) {
                firstTranslation = saved;
            }
        }

        return offerMapper.toResponse(offer, firstTranslation);
    }

    @Override
    public List<OfferResponse> findByServicePoint(UUID servicePointId, String locale) {
        return offerRepository.findByServicePointIdAndActiveTrue(servicePointId).stream()
                .map(offer -> offerMapper.toResponse(offer, translationResolver.resolve(offer.getId(), locale)))
                .toList();
    }
}
