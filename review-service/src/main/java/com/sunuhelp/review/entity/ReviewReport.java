package com.sunuhelp.review.entity;

import com.sunuhelp.common.entity.BaseEntity;
import com.sunuhelp.review.enums.ReportReason;
import com.sunuhelp.review.enums.ReportStatus;
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

/** Signalement d'un avis abusif - meme pattern que EntityReport dans entity-service. */
@Entity
@Table(name = "review_reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReport extends BaseEntity {

    @Column(name = "review_id", nullable = false)
    private UUID reviewId;

    /** Nullable - un Visiteur non connecte peut signaler un avis choquant. */
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

    @Column(name = "reviewed_by_account_id")
    private UUID reviewedByAccountId;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    public static ReviewReport create(UUID reviewId, UUID reporterAccountId, ReportReason reason, String comment) {
        ReviewReport report = new ReviewReport();
        report.reviewId = reviewId;
        report.reporterAccountId = reporterAccountId;
        report.reason = reason;
        report.comment = comment;
        report.status = ReportStatus.PENDING;
        return report;
    }

    public void markReviewed(UUID reviewerId) {
        this.status = ReportStatus.REVIEWED;
        this.reviewedByAccountId = reviewerId;
        this.reviewedAt = LocalDateTime.now();
    }

    public void dismiss(UUID reviewerId) {
        this.status = ReportStatus.DISMISSED;
        this.reviewedByAccountId = reviewerId;
        this.reviewedAt = LocalDateTime.now();
    }
}
