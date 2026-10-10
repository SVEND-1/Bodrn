package org.example.bodrn.category.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    @Query("""
            SELECT c FROM CategoryEntity c
            WHERE (:search IS NULL OR :search = ''
                   OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    List<CategoryEntity> findAllFiltered(@Param("search") String search);
}