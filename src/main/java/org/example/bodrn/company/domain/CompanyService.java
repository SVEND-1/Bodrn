package org.example.bodrn.company.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodrn.company.api.dto.request.CompanyCreateRequest;
import org.example.bodrn.company.api.dto.request.CompanyUpdateRequest;
import org.example.bodrn.company.api.dto.response.CompanyResponse;
import org.example.bodrn.company.db.CompanyEntity;
import org.example.bodrn.company.db.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Transactional(readOnly = true)
    public CompanyResponse get() {
        CompanyEntity entity = companyRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("Информация о компании ещё не заполнена"));
        return companyMapper.toResponse(entity);
    }

    public CompanyResponse create(CompanyCreateRequest request) {
        if (companyRepository.count() > 0) {
            throw new IllegalStateException("Информация о компании уже существует");
        }

        CompanyEntity entity = companyMapper.toEntity(request);
        entity.setUpdatedAt(LocalDateTime.now());

        CompanyEntity saved = companyRepository.save(entity);
        log.info("Создана карточка компании id={}, name={}", saved.getId(), saved.getName());
        return companyMapper.toResponse(saved);
    }

    @Transactional
    public CompanyResponse update(CompanyUpdateRequest request) {
        CompanyEntity entity = companyRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("Информация о компании ещё не заполнена"));

        companyMapper.update(entity, request);
        entity.setUpdatedAt(LocalDateTime.now());

        CompanyEntity saved = companyRepository.save(entity);
        log.info("Обновлена карточка компании id={}", saved.getId());
        return companyMapper.toResponse(saved);
    }

    public void delete() {
        companyRepository.findFirstByOrderByIdAsc()
                .ifPresent(companyRepository::delete);
    }
}