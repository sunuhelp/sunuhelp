package com.sunuhelp.entity.dto.response;

import com.sunuhelp.entity.enums.ReportReason;
import com.sunuhelp.entity.enums.ReportStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class EntityReportResponse {
    private UUID id;
    private UUID entityId;
    private ReportReason reason;
    private String comment;
    private ReportStatus status;
}
