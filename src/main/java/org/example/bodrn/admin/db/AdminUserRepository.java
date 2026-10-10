package org.example.bodrn.admin.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Long> {

    Optional<AdminUserEntity> findByUserName(String userName);

    Optional<AdminUserEntity> findByUserNameIgnoreCase(String userName);

    boolean existsByUserNameIgnoreCase(String userName);

    long countByIsActiveTrue();
}