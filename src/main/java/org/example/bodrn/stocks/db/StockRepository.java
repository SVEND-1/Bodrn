package org.example.bodrn.stocks.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface StockRepository extends JpaRepository<StockEntity, Long> {

    Page<StockEntity> findAllByOrderByValidFromDesc(Pageable pageable);

    @Query("""
            SELECT s FROM StockEntity s
            WHERE (:now >= s.validFrom OR s.validFrom IS NULL)
              AND (:now <= s.validTo   OR s.validTo   IS NULL)
            ORDER BY s.validFrom DESC
            """)
    Page<StockEntity> findActive(@Param("now") LocalDateTime now, Pageable pageable);
}