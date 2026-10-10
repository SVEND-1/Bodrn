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
}