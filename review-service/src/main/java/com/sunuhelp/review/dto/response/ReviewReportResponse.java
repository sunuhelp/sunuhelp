package com.sunuhelp.review.dto.response;

import com.sunuhelp.review.enums.ReportReason;
import com.sunuhelp.review.enums.ReportStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ReviewReportResponse {
    private UUID id;
    private UUID reviewId;
    private ReportReason reason;
    private String comment;
    private ReportStatus status;
}
