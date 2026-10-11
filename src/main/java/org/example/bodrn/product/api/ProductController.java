package org.example.bodrn.product.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodrn.product.api.dto.request.ProductCreateRequest;
import org.example.bodrn.product.api.dto.request.ProductFilterRequest;
import org.example.bodrn.product.api.dto.request.ProductUpdateRequest;
import org.example.bodrn.product.api.dto.response.ProductResponse;
import org.example.bodrn.product.api.dto.response.ProductShortResponse;
import org.example.bodrn.product.domain.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductShortResponse>> findAll(@ModelAttribute ProductFilterRequest filter) {
        return ResponseEntity.ok(productService.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findOne(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> create(@Valid @ModelAttribute ProductCreateRequest request) {
        return ResponseEntity.ok(productService.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}