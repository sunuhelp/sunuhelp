package com.sunuhelp.review.service.impl;

import com.sunuhelp.review.dto.request.CreateResponseRequest;
import com.sunuhelp.review.dto.request.CreateReviewRequest;
import com.sunuhelp.review.dto.response.ReviewResponseDto;
import com.sunuhelp.review.entity.Review;
import com.sunuhelp.review.entity.ReviewResponse;
import com.sunuhelp.review.event.EventProducer;
import com.sunuhelp.review.event.ReviewCreatedEvent;
import com.sunuhelp.review.event.ReviewResponseCreatedEvent;
import com.sunuhelp.review.exception.ResponseAlreadyExistsException;
import com.sunuhelp.review.exception.ReviewAlreadyExistsException;
import com.sunuhelp.review.exception.ReviewNotFoundException;
import com.sunuhelp.review.mapper.ReviewMapper;
import com.sunuhelp.review.repository.ReviewRepository;
import com.sunuhelp.review.repository.ReviewResponseRepository;
import com.sunuhelp.review.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewResponseRepository responseRepository;
    private final ReviewMapper reviewMapper;
    private final EventProducer eventProducer;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                              ReviewResponseRepository responseRepository,
                              ReviewMapper reviewMapper,
                              EventProducer eventProducer) {
        this.reviewRepository = reviewRepository;
        this.responseRepository = responseRepository;
        this.reviewMapper = reviewMapper;
        this.eventProducer = eventProducer;
    }

    @Override
    @Transactional
    public ReviewResponseDto create(UUID entityId, CreateReviewRequest request, UUID reviewerAccountId) {
        if (reviewRepository.existsByEntityIdAndReviewerAccountId(entityId, reviewerAccountId)) {
            throw new ReviewAlreadyExistsException();
        }

        Review review = Review.create(entityId, reviewerAccountId, request.getRating(), request.getComment());
        reviewRepository.saveAndFlush(review);

        eventProducer.publish(ReviewCreatedEvent.of(review.getId(), entityId, request.getRating()));

        return reviewMapper.toResponse(review, null);
    }

    @Override
    public Page<ReviewResponseDto> findByEntity(UUID entityId, Pageable pageable) {
        return reviewRepository.findByEntityIdAndActiveTrue(entityId, pageable)
                .map(review -> {
                    String responseContent = responseRepository.findByReviewId(review.getId())
                            .map(ReviewResponse::getContent)
                            .orElse(null);
                    return reviewMapper.toResponse(review, responseContent);
                });
    }

    @Override
    @Transactional
    public ReviewResponseDto respond(UUID reviewId, CreateResponseRequest request, UUID responderAccountId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

        if (responseRepository.existsByReviewId(reviewId)) {
            throw new ResponseAlreadyExistsException();
        }

        ReviewResponse response = ReviewResponse.create(reviewId, request.getContent());
        responseRepository.save(response);

        eventProducer.publish(ReviewResponseCreatedEvent.of(reviewId, review.getReviewerAccountId()));

        return reviewMapper.toResponse(review, response.getContent());
    }
}
