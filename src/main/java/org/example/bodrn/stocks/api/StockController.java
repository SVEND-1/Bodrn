package org.example.bodrn.stocks.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodrn.reviews.api.dto.response.PageResponse;
import org.example.bodrn.stocks.api.dto.request.StockCreateRequest;
import org.example.bodrn.stocks.api.dto.request.StockUpdateRequest;
import org.example.bodrn.stocks.api.dto.response.StockResponse;
import org.example.bodrn.stocks.api.dto.response.StockShortResponse;
import org.example.bodrn.stocks.domain.StockService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<PageResponse<StockShortResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean onlyActive) {
        return ResponseEntity.ok(stockService.findAll(page, size, onlyActive));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findOne(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StockResponse> create(@Valid @ModelAttribute StockCreateRequest request) {
        return ResponseEntity.ok(stockService.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StockResponse> update(
            @PathVariable Long id,
            @Valid @ModelAttribute StockUpdateRequest request) {
        return ResponseEntity.ok(stockService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}