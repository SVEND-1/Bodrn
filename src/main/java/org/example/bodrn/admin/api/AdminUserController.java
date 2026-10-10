package org.example.bodrn.admin.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodrn.admin.api.dto.request.AdminUserCreateRequest;
import org.example.bodrn.admin.api.dto.request.AdminUserUpdateRequest;
import org.example.bodrn.admin.api.dto.response.AdminUserResponse;
import org.example.bodrn.admin.domain.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> list() {
        return ResponseEntity.ok(adminUserService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.get(id));
    }

    @PostMapping
    public ResponseEntity<AdminUserResponse> create(@Valid @RequestBody AdminUserCreateRequest request) {
        return ResponseEntity.ok(adminUserService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUserResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody AdminUserUpdateRequest request) {
        return ResponseEntity.ok(adminUserService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}