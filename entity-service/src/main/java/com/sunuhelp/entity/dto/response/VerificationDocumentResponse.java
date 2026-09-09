package com.sunuhelp.entity.dto.response;

import com.sunuhelp.entity.enums.DocumentStatus;
import com.sunuhelp.entity.enums.DocumentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class VerificationDocumentResponse {
    private UUID id;
    private UUID entityId;
    private DocumentType documentType;
    private String documentNumber;
    private DocumentStatus status;
    private String rejectionReason;
    private LocalDateTime reviewedAt;
}
