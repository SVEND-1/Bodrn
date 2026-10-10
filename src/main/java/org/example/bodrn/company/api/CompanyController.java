package org.example.bodrn.company.api;

import lombok.RequiredArgsConstructor;
import org.example.bodrn.company.api.dto.response.CompanyResponse;
import org.example.bodrn.company.domain.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<CompanyResponse> get() {
        return ResponseEntity.ok(companyService.get());
    }
}