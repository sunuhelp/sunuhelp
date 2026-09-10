package com.sunuhelp.review.repository;

import com.sunuhelp.review.entity.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, UUID> {
}
