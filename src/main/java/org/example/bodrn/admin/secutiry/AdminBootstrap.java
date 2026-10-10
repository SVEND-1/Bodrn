package org.example.bodrn.admin.secutiry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodrn.admin.api.dto.request.AdminUserCreateRequest;
import org.example.bodrn.admin.db.AdminUserRepository;
import org.example.bodrn.admin.domain.AdminUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminBootstrap {

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:}")
    private String adminPassword;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Bean
    CommandLineRunner initAdmin(AdminUserRepository repo, AdminUserService service) {
        return args -> {
            if (repo.findByUserName(adminUsername).isPresent()) {
                return;
            }

            boolean isProd = activeProfile.contains("prod");
            String password = adminPassword;

            if (password == null || password.isBlank()) {
                if (isProd) {
                    throw new IllegalStateException(
                            "admin.password is not set in prod profile — refusing to start");
                }
                password = "admin123";
            }

            service.create(new AdminUserCreateRequest(adminUsername, password, true));
        };
    }
}