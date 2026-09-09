package com.sunuhelp.entity.entity;

import com.sunuhelp.common.entity.BaseEntity;
import com.sunuhelp.entity.enums.ReportReason;
import com.sunuhelp.entity.enums.ReportStatus;
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

/** Signalement d'une fiche erronee par un Usager ou un Visiteur - alimente la file de moderation. */
@Entity
@Table(name = "entity_reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityReport extends BaseEntity {

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    /** Nullable - un Visiteur non connecte peut signaler une fiche. */
    @Column(name = "reporter_account_id")
    private UUID reporterAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportReason reason;

    @Column
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    /** Reference logique vers Account.id de l'Administrateur ayant traite ce signalement. */
    @Column(name = "reviewed_by_account_id")
    private UUID reviewedByAccountId;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    public static EntityReport create(UUID entityId, UUID reporterAccountId, ReportReason reason, String comment) {
        EntityReport report = new EntityReport();
        report.entityId = entityId;
        report.reporterAccountId = reporterAccountId;
        report.reason = reason;
        report.comment = comment;
        report.status = ReportStatus.PENDING;
        return report;
    }

    /** Marque le signalement comme traite et fonde - trace l'Administrateur responsable. */
    public void markReviewed(UUID reviewerId) {
        this.status = ReportStatus.REVIEWED;
        this.reviewedByAccountId = reviewerId;
        this.reviewedAt = LocalDateTime.now();
    }

    /** Rejette le signalement comme non fonde. */
    public void dismiss(UUID reviewerId) {
        this.status = ReportStatus.DISMISSED;
        this.reviewedByAccountId = reviewerId;
        this.reviewedAt = LocalDateTime.now();
    }
}
