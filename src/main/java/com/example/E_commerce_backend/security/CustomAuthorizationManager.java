package com.example.E_commerce_backend.security;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.example.E_commerce_backend.entity.Permission;
import com.example.E_commerce_backend.entity.Role;
import com.example.E_commerce_backend.repository.RoleRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final RoleRepository roleRepository;
    private final String ROLE_PREFIX = "ROLE_";

    @Override
    public AuthorizationResult authorize(Supplier<? extends @Nullable Authentication> authSupplier,
            RequestAuthorizationContext context) {
        // get authentication
        Authentication authentication = authSupplier.get();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            log.debug("Authorization Failed: no authenticated user");
            return new AuthorizationDecision(false);
        }

        // get request + path + method
        HttpServletRequest request = context.getRequest();
        String path = request.getServletPath();
        String method = request.getMethod();
        String username = authentication.getName();

        // compare authorities with request
        try {
            boolean granted = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .filter(role -> role.startsWith(ROLE_PREFIX))
                    .flatMap(role -> getPermissions(role.substring(5)).stream())
                    .anyMatch(p -> pathMatcher.match(p.getPath(), path) && p.getMethod().equalsIgnoreCase(method));
            if (!granted) {
                log.info("Authorization denied for user={} method={} path={}", username, method, path);
            }

            return new AuthorizationDecision(granted);
        } catch (Exception e) {
            log.debug("Authorization check failed for user={} method={} path={}", username, method, path);

            return new AuthorizationDecision(false);
        }
    }

    private List<Permission> getPermissions(String roleName) {
        List<Permission> permissions = roleRepository.findByName(roleName).map(Role::getPermissions).map(ArrayList::new)
                .orElse(new ArrayList<Permission>());

        return permissions;
    }

}
