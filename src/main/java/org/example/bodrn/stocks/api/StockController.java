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
}