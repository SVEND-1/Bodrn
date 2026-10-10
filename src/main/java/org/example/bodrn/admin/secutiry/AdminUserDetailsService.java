package org.example.bodrn.admin.secutiry;

import lombok.RequiredArgsConstructor;
import org.example.bodrn.admin.db.AdminUserEntity;
import org.example.bodrn.admin.db.AdminUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException("empty username");
        }
        AdminUserEntity admin = adminUserRepository.findByUserName(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("admin not found"));
        return new AdminUserDetails(admin);
    }
}