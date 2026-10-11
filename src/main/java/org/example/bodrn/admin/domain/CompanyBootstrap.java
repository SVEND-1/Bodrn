package org.example.bodrn.admin.domain;

import lombok.RequiredArgsConstructor;
import org.example.bodrn.company.api.dto.request.CompanyCreateRequest;
import org.example.bodrn.company.db.CompanyRepository;
import org.example.bodrn.company.domain.CompanyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CompanyBootstrap {

    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    @Bean
    CommandLineRunner initCompanyData() {
        return args -> {
            if (companyRepository.count() == 0) {
                companyService.create(new CompanyCreateRequest(
                        "BODR*N",
                        "Лучшая кофейня в городе",
                        "ул. Примерная, 1",
                        "+7 (999) 000-00-00",
                        "08:00 - 22:00",
                        "09:00 - 23:00"
                ));
            }
        };
    }
}