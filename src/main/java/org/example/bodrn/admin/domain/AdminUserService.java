package org.example.bodrn.admin.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodrn.admin.api.dto.request.AdminUserCreateRequest;
import org.example.bodrn.admin.api.dto.request.AdminUserUpdateRequest;
import org.example.bodrn.admin.api.dto.response.AdminUserResponse;
import org.example.bodrn.admin.db.AdminUserEntity;
import org.example.bodrn.admin.db.AdminUserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminUserMapper adminUserMapper;

    @Transactional(readOnly = true)
    public List<AdminUserResponse> list() {
        return adminUserMapper.toResponseList(adminUserRepository.findAll());
    }

    @Transactional(readOnly = true)
    public AdminUserResponse get(Long id) {
        return adminUserMapper.toResponse(findAdmin(id));
    }

    @Transactional
    public AdminUserResponse create(AdminUserCreateRequest request) {
        String normalized = request.userName() == null ? null : request.userName().trim();
        if (normalized == null || normalized.isEmpty()) {
            throw new IllegalArgumentException("username is required");
        }
        if (request.password() == null || request.password().length() < 8) {
            throw new IllegalArgumentException("password must be at least 8 chars");
        }
        if (adminUserRepository.existsByUserNameIgnoreCase(normalized)) {
            throw new IllegalStateException("username already exists");
        }

        LocalDateTime now = LocalDateTime.now();

        AdminUserEntity admin = AdminUserEntity.builder()
                .userName(normalized)
                .passwordHash(passwordEncoder.encode(request.password()))
                .isActive(request.isActive() == null || request.isActive())
                .createdAt(now)
                .updatedAt(now)
                .build();

        AdminUserEntity saved = adminUserRepository.save(admin);
        log.info("Создан администратор id={}, userName={}", saved.getId(), saved.getUserName());
        return adminUserMapper.toResponse(saved);
    }

    @Transactional
    public AdminUserResponse update(Long id, AdminUserUpdateRequest request) {
        AdminUserEntity current = findAdmin(id);

        if (request.userName() != null) {
            String normalized = request.userName().trim();
            if (normalized.isEmpty()) {
                throw new IllegalArgumentException("username cannot be empty");
            }
            if (!normalized.equalsIgnoreCase(current.getUserName())
                    && adminUserRepository.existsByUserNameIgnoreCase(normalized)) {
                throw new IllegalStateException("username already exists");
            }
            current.setUserName(normalized);
        }

        if (request.password() != null) {
            if (request.password().length() < 8) {
                throw new IllegalArgumentException("password must be at least 8 chars");
            }
            current.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        if (request.isActive() != null) {
            current.setIsActive(request.isActive());
        }

        current.setUpdatedAt(LocalDateTime.now());

        try {
            AdminUserEntity saved = adminUserRepository.save(current);
            log.info("Обновлён администратор id={}", saved.getId());
            return adminUserMapper.toResponse(saved);
        } catch (DataIntegrityViolationException e) {
            log.warn("admin update failed for id={}", id, e);
            throw new IllegalStateException("cannot update admin user");
        }
    }

    @Transactional
    public void delete(Long id) {
        AdminUserEntity current = findAdmin(id);

        if (Boolean.TRUE.equals(current.getIsActive())
                && adminUserRepository.countByIsActiveTrue() <= 1) {
            throw new IllegalStateException("cannot delete the last active admin");
        }

        adminUserRepository.delete(current);
        log.info("Удалён администратор id={}", id);
    }

    @Transactional(readOnly = true)
    public AdminUserEntity findByUserName(String userName) {
        if (userName == null || userName.isBlank()) return null;
        return adminUserRepository.findByUserNameIgnoreCase(userName.trim()).orElse(null);
    }

    @Transactional
    public void touchLastLogin(Long id) {
        adminUserRepository.findById(id).ifPresent(a -> {
            a.setLastLoginAt(LocalDateTime.now());
            a.setUpdatedAt(LocalDateTime.now());
            adminUserRepository.save(a);
        });
    }


    private AdminUserEntity findAdmin(Long id) {
        return adminUserRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Администратор не найден: " + id));
    }
}