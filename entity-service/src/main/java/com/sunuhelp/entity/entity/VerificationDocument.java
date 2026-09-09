package com.sunuhelp.entity.entity;

import com.sunuhelp.common.entity.BaseEntity;
import com.sunuhelp.entity.enums.DocumentStatus;
import com.sunuhelp.entity.enums.DocumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Piece justificative soumise pour obtenir le badge Verifiee.
 * Entierement facultative, jamais requise pour publier une fiche.
 * Le fichier lui-meme (file_url) vit dans media-service, pas ici -
 * la relation se fait via media_files.owner_id = verification_documents.id.
 */
@Entity
@Table(name = "verification_documents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationDocument extends BaseEntity {

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    /** Facultatif meme si le type de document est choisi. */
    @Column(name = "document_number")
    private String documentNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    /** Renseigne uniquement si status = REJECTED, pour que l'Entite sache quoi corriger. */
    @Column(name = "rejection_reason")
    private String rejectionReason;

    /** Reference logique vers Account.id de l'Administrateur ayant traite ce document. */
    @Column(name = "reviewed_by_account_id")
    private UUID reviewedByAccountId;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    public static VerificationDocument submit(UUID entityId, DocumentType documentType, String documentNumber) {
        VerificationDocument document = new VerificationDocument();
        document.entityId = entityId;
        document.documentType = documentType;
        document.documentNumber = documentNumber;
        document.status = DocumentStatus.PENDING;
        return document;
    }

    /** Approuve le document - trace l'Administrateur responsable et la date, pour audit. */
    public void approve(UUID reviewerId) {
        this.status = DocumentStatus.APPROVED;
        this.reviewedByAccountId = reviewerId;
        this.reviewedAt = LocalDateTime.now();
    }

    /** Rejette le document avec un motif explicite, pour que l'Entite puisse corriger. */
    public void reject(UUID reviewerId, String reason) {
        this.status = DocumentStatus.REJECTED;
        this.rejectionReason = reason;
        this.reviewedByAccountId = reviewerId;
        this.reviewedAt = LocalDateTime.now();
    }
}
