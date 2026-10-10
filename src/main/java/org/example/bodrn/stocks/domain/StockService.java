package org.example.bodrn.stocks.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodrn.comm.PhotoService;
import org.example.bodrn.reviews.api.dto.response.PageResponse;
import org.example.bodrn.stocks.api.dto.request.StockCreateRequest;
import org.example.bodrn.stocks.api.dto.request.StockUpdateRequest;
import org.example.bodrn.stocks.api.dto.response.StockResponse;
import org.example.bodrn.stocks.api.dto.response.StockShortResponse;
import org.example.bodrn.stocks.db.StockEntity;
import org.example.bodrn.stocks.db.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final PhotoService photoService;

    public StockResponse create(StockCreateRequest request) {
        validateDates(request.validFrom(), request.validTo());

        StockEntity entity = stockMapper.toEntity(request);

        if (request.photo() != null && !request.photo().isEmpty()) {
            entity.setPhoto(uploadPhoto(request.photo()));
        }

        StockEntity saved = stockRepository.save(entity);
        log.info("Создана акция id={}, title={}", saved.getId(), saved.getTitle());
        return stockMapper.toResponse(saved);
    }

    @Transactional
    public StockResponse update(Long id, StockUpdateRequest request) {
        StockEntity entity = findStock(id);

        stockMapper.update(entity, request);

        validateDates(entity.getValidFrom(), entity.getValidTo());

        if (Boolean.TRUE.equals(request.removePhoto())) {
            if (entity.getPhoto() != null) {
                photoService.delete(entity.getPhoto());
                entity.setPhoto(null);
            }
        } else if (request.photo() != null && !request.photo().isEmpty()) {
            if (entity.getPhoto() != null) {
                photoService.delete(entity.getPhoto());
            }
            entity.setPhoto(uploadPhoto(request.photo()));
        }

        StockEntity saved = stockRepository.save(entity);
        log.info("Обновлена акция id={}", saved.getId());
        return stockMapper.toResponse(saved);
    }

    public PageResponse<StockShortResponse> findAll(int page, int size, boolean onlyActive) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        Page<StockEntity> result = onlyActive
                ? stockRepository.findActive(LocalDateTime.now(), pageable)
                : stockRepository.findAllByOrderByValidFromDesc(pageable);
        return PageResponse.of(result, stockMapper::toShortResponse);
    }

    public StockResponse findOne(Long id) {
        return stockMapper.toResponse(findStock(id));
    }

    @Transactional
    public void delete(Long id) {
        StockEntity entity = findStock(id);
        if (entity.getPhoto() != null) {
            photoService.delete(entity.getPhoto());
        }
        stockRepository.delete(entity);
        log.info("Удалена акция id={}", id);
    }


    private StockEntity findStock(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Акция не найдена: " + id));
    }

    private String uploadPhoto(MultipartFile file) {
        photoService.compress(file);
        return photoService.save(file);
    }

    private void validateDates(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("validFrom не может быть позже validTo");
        }
    }
}