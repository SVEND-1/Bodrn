package org.example.bodrn.admin.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodrn.category.api.dto.request.CategoryCreateRequest;
import org.example.bodrn.category.api.dto.request.CategoryUpdateRequest;
import org.example.bodrn.category.api.dto.response.CategoryResponse;
import org.example.bodrn.category.domain.CategoryService;
import org.example.bodrn.company.api.dto.request.CompanyCreateRequest;
import org.example.bodrn.company.api.dto.request.CompanyUpdateRequest;
import org.example.bodrn.company.api.dto.response.CompanyResponse;
import org.example.bodrn.company.domain.CompanyService;
import org.example.bodrn.product.api.dto.request.ProductCreateRequest;
import org.example.bodrn.product.api.dto.request.ProductUpdateRequest;
import org.example.bodrn.product.api.dto.response.ProductResponse;
import org.example.bodrn.product.domain.ProductService;
import org.example.bodrn.reviews.domain.ReviewService;
import org.example.bodrn.stocks.api.dto.request.StockCreateRequest;
import org.example.bodrn.stocks.api.dto.request.StockUpdateRequest;
import org.example.bodrn.stocks.api.dto.response.StockResponse;
import org.example.bodrn.stocks.domain.StockService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CompanyService companyService;
    private final StockService stockService;
    private final ReviewService reviewService;

    // ===================== PRODUCTS =====================

    @PostMapping(path = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> createProduct(@Valid @ModelAttribute ProductCreateRequest request) {
        return ResponseEntity.ok(productService.create(request));
    }

    @PutMapping(path = "/products/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @ModelAttribute ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ===================== CATEGORIES =====================

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @Valid @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ===================== COMPANY =====================

    @PostMapping("/company")
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyCreateRequest request) {
        return ResponseEntity.ok(companyService.create(request));
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyResponse> updateCompany(@Valid @RequestBody CompanyUpdateRequest request) {
        return ResponseEntity.ok(companyService.update(request));
    }

    @DeleteMapping("/company")
    public ResponseEntity<Void> deleteCompany() {
        companyService.delete();
        return ResponseEntity.noContent().build();
    }

    // ===================== STOCKS =====================

    @PostMapping(path = "/stocks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StockResponse> createStock(@Valid @ModelAttribute StockCreateRequest request) {
        return ResponseEntity.ok(stockService.create(request));
    }

    @PutMapping(path = "/stocks/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StockResponse> updateStock(@PathVariable Long id,
                                                     @Valid @ModelAttribute StockUpdateRequest request) {
        return ResponseEntity.ok(stockService.update(id, request));
    }

    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ===================== REVIEW =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}