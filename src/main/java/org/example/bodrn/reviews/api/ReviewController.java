package org.example.bodrn.reviews.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodrn.reviews.api.dto.request.ReviewCreateRequest;
import org.example.bodrn.reviews.api.dto.response.PageResponse;
import org.example.bodrn.reviews.api.dto.response.ReviewResponse;
import org.example.bodrn.reviews.domain.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewCreateRequest request) {
        return ResponseEntity.ok(reviewService.create(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReviewResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.findAll(page, size));
    }

}