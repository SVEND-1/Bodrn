package org.example.bodrn.product.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("""
            SELECT p FROM ProductEntity p
            WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
              AND (:search IS NULL OR :search = ''
                   OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    List<ProductEntity> findAllFiltered(@Param("categoryId") Long categoryId,
                                        @Param("search") String search);
}