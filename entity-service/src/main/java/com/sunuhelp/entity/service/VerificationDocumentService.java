package com.sunuhelp.entity.service;

import com.sunuhelp.entity.dto.request.ReviewDocumentRequest;
import com.sunuhelp.entity.dto.request.SubmitDocumentRequest;
import com.sunuhelp.entity.dto.response.VerificationDocumentResponse;

import java.util.List;
import java.util.UUID;

public interface VerificationDocumentService {

    VerificationDocumentResponse submit(UUID entityId, SubmitDocumentRequest request, UUID requesterAccountId);

    List<VerificationDocumentResponse> findByEntity(UUID entityId, UUID requesterAccountId);

    /** Reserve a l'Administrateur - approuve ou rejette, accorde/retire le badge Verifiee en consequence. */
    VerificationDocumentResponse review(UUID documentId, ReviewDocumentRequest request, UUID reviewerAccountId);
}
