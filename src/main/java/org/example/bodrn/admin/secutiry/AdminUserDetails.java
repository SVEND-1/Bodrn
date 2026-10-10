package org.example.bodrn.admin.secutiry;

import lombok.RequiredArgsConstructor;
import org.example.bodrn.admin.db.AdminUserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class AdminUserDetails implements UserDetails {

    private final AdminUserEntity admin;

    public AdminUserEntity getAdmin() {
        return admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getPassword() {
        return admin.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return admin.getUserName();
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(admin.getIsActive());
    }

    @Override
    public String toString() {
        return "AdminUserDetails{username='" + admin.getUserName() + "'}";
    }
}