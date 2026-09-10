package com.sunuhelp.review.service.impl;

import com.sunuhelp.review.dto.request.CreateReviewReportRequest;
import com.sunuhelp.review.dto.response.ReviewReportResponse;
import com.sunuhelp.review.entity.ReviewReport;
import com.sunuhelp.review.exception.ReviewNotFoundException;
import com.sunuhelp.review.mapper.ReviewReportMapper;
import com.sunuhelp.review.repository.ReviewReportRepository;
import com.sunuhelp.review.repository.ReviewRepository;
import com.sunuhelp.review.service.ReviewReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ReviewReportServiceImpl implements ReviewReportService {

    private final ReviewReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewReportMapper reportMapper;

    public ReviewReportServiceImpl(ReviewReportRepository reportRepository,
                                    ReviewRepository reviewRepository,
                                    ReviewReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reviewRepository = reviewRepository;
        this.reportMapper = reportMapper;
    }

    @Override
    @Transactional
    public ReviewReportResponse create(UUID reviewId, CreateReviewReportRequest request, UUID reporterAccountId) {
        reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

        ReviewReport report = ReviewReport.create(reviewId, reporterAccountId, request.getReason(), request.getComment());
        reportRepository.save(report);

        return reportMapper.toResponse(report);
    }
}
