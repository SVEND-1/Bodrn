package org.example.bodrn.category.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodrn.category.api.dto.request.CategoryCreateRequest;
import org.example.bodrn.category.api.dto.request.CategoryUpdateRequest;
import org.example.bodrn.category.api.dto.response.CategoryResponse;
import org.example.bodrn.category.api.dto.response.CategoryShortResponse;
import org.example.bodrn.category.domain.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryShortResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findOne(id));
    }
}