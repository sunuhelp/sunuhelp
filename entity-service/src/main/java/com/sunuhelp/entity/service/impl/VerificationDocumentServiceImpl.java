package com.sunuhelp.entity.service.impl;

import com.sunuhelp.entity.dto.request.ReviewDocumentRequest;
import com.sunuhelp.entity.dto.request.SubmitDocumentRequest;
import com.sunuhelp.entity.dto.response.VerificationDocumentResponse;
import com.sunuhelp.entity.entity.BusinessEntity;
import com.sunuhelp.entity.entity.VerificationDocument;
import com.sunuhelp.entity.enums.DocumentStatus;
import com.sunuhelp.entity.event.EventProducer;
import com.sunuhelp.entity.event.VerificationDocumentReviewedEvent;
import com.sunuhelp.entity.exception.DocumentNotFoundException;
import com.sunuhelp.entity.mapper.VerificationDocumentMapper;
import com.sunuhelp.entity.repository.BusinessEntityRepository;
import com.sunuhelp.entity.repository.VerificationDocumentRepository;
import com.sunuhelp.entity.service.EntityOwnershipValidator;
import com.sunuhelp.entity.service.VerificationDocumentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class VerificationDocumentServiceImpl implements VerificationDocumentService {

    private final VerificationDocumentRepository documentRepository;
    private final BusinessEntityRepository entityRepository;
    private final EntityOwnershipValidator ownershipValidator;
    private final VerificationDocumentMapper documentMapper;
    private final EventProducer eventProducer;

    public VerificationDocumentServiceImpl(VerificationDocumentRepository documentRepository,
                                            BusinessEntityRepository entityRepository,
                                            EntityOwnershipValidator ownershipValidator,
                                            VerificationDocumentMapper documentMapper,
                                            EventProducer eventProducer) {
        this.documentRepository = documentRepository;
        this.entityRepository = entityRepository;
        this.ownershipValidator = ownershipValidator;
        this.documentMapper = documentMapper;
        this.eventProducer = eventProducer;
    }

    @Override
    @Transactional
    public VerificationDocumentResponse submit(UUID entityId, SubmitDocumentRequest request, UUID requesterAccountId) {
        ownershipValidator.assertOwner(entityId, requesterAccountId);

        VerificationDocument document = VerificationDocument.submit(
                entityId, request.getDocumentType(), request.getDocumentNumber());
        documentRepository.save(document);

        return documentMapper.toResponse(document);
    }

    @Override
    public List<VerificationDocumentResponse> findByEntity(UUID entityId, UUID requesterAccountId) {
        ownershipValidator.assertOwner(entityId, requesterAccountId);

        return documentRepository.findByEntityId(entityId).stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public VerificationDocumentResponse review(UUID documentId, ReviewDocumentRequest request, UUID reviewerAccountId) {
        VerificationDocument document = documentRepository.findById(documentId)
                .orElseThrow(DocumentNotFoundException::new);

        if (request.isApproved()) {
            document.approve(reviewerAccountId);
            entityRepository.findById(document.getEntityId()).ifPresent(BusinessEntity::verify);
        } else {
            document.reject(reviewerAccountId, request.getRejectionReason());
        }
        documentRepository.save(document);

        eventProducer.publish(VerificationDocumentReviewedEvent.of(
                document.getId(), document.getEntityId(), document.getStatus()));

        return documentMapper.toResponse(document);
    }
}
