package org.example.bodrn.reviews.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Page<ReviewEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}