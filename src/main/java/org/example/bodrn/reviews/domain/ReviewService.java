package org.example.bodrn.reviews.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodrn.reviews.api.dto.request.ReviewCreateRequest;
import org.example.bodrn.reviews.api.dto.response.PageResponse;
import org.example.bodrn.reviews.api.dto.response.ReviewResponse;
import org.example.bodrn.reviews.db.ReviewEntity;
import org.example.bodrn.reviews.db.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewResponse create(ReviewCreateRequest request) {
        ReviewEntity entity = reviewMapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        ReviewEntity saved = reviewRepository.save(entity);
        return reviewMapper.toResponse(saved);
    }

    public PageResponse<ReviewResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        Page<ReviewEntity> pageResult = reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
        return PageResponse.of(pageResult, reviewMapper::toResponse);
    }

    public void delete(Long id) {
        ReviewEntity entity = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Отзыв не найден: " + id));
        reviewRepository.delete(entity);
    }
}