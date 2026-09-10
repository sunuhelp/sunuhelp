package com.sunuhelp.review.dto.request;

import com.sunuhelp.review.enums.ReportReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewReportRequest {

    @NotNull
    private ReportReason reason;

    private String comment;
}
