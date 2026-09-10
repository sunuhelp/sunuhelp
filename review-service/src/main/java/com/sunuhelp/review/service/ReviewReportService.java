package com.sunuhelp.review.service;

import com.sunuhelp.review.dto.request.CreateReviewReportRequest;
import com.sunuhelp.review.dto.response.ReviewReportResponse;

import java.util.UUID;

public interface ReviewReportService {

    ReviewReportResponse create(UUID reviewId, CreateReviewReportRequest request, UUID reporterAccountId);
}
