package org.example.bodrn.admin.secutiry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodrn.admin.api.dto.request.LoginRequestDto;
import org.example.bodrn.admin.api.dto.response.AdminDto;
import org.example.bodrn.admin.db.AdminUserEntity;
import org.example.bodrn.admin.db.AdminUserRepository;
import org.example.bodrn.security.LoginRateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthenticationManager authenticationManager;
    private final AdminUserRepository adminUserRepository;
    private final LoginRateLimiter rateLimiter;

    /** Отдаёт CSRF-токен, чтобы фронт мог положить его в заголовок. */
    @GetMapping("/csrf")
    public ResponseEntity<?> csrf(CsrfToken token) {
        return ResponseEntity.ok(Map.of(
                "headerName", token.getHeaderName(),
                "token", token.getToken()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto dto,
                                   HttpServletRequest request) {
        String username = dto.getUsername();
        String clientIp = clientIp(request);

        if (!rateLimiter.tryConsume(clientIp, username)) {
            log.warn("Login rate limit exceeded ip={} user={}", clientIp, username);
            return ResponseEntity.status(429)
                    .body(Map.of("error", "too many attempts, try later"));
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, dto.getPassword())
            );

            // Защита от session fixation: если сессия уже есть — меняем её id,
            // если нет — создаём новую. invalidate() здесь НЕ вызываем,
            // иначе changeSessionId() упадёт с "no session associated".
            HttpSession session = request.getSession(false);
            if (session != null) {
                request.changeSessionId();
            } else {
                session = request.getSession(true);
            }

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context);

            adminUserRepository.findByUserName(username).ifPresent(admin -> {
                admin.setLastLoginAt(LocalDateTime.now());
                adminUserRepository.save(admin);
            });

            AdminUserEntity admin = adminUserRepository.findByUserName(username).orElseThrow();
            log.info("Admin login success user={} ip={}", username, clientIp);
            rateLimiter.reset(clientIp, username);
            return ResponseEntity.ok(toAdminDto(admin));

        } catch (BadCredentialsException e) {
            log.warn("Admin login failed user={} ip={}", username, clientIp);
            return ResponseEntity.status(401)
                    .body(Map.of("error", "invalid credentials"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        AdminUserEntity admin = adminUserRepository.findByUserName(auth.getName()).orElse(null);
        if (admin == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(toAdminDto(admin));
    }

    private AdminDto toAdminDto(AdminUserEntity admin) {
        return AdminDto.builder()
                .id(admin.getId())
                .username(admin.getUserName())
                .isActive(admin.getIsActive())
                .createdAt(admin.getCreatedAt())
                .build();
    }

    private String clientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}