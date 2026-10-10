package org.example.bodrn.company.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    /** Берём единственную строку (самую раннюю). */
    Optional<CompanyEntity> findFirstByOrderByIdAsc();
}