package com.authservice.jwt;

import com.authservice.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    //private final UserDetailsService userDetailsService;

    @Autowired
    private ApplicationContext applicationContext;

    // Method to lazily fetch the UserService bean from the ApplicationContext
    // This is done to avoid Circular Dependency issues
    private UserDetailsService userDetailsService() {
        return applicationContext.getBean(UserDetailsService.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/auth")) {
            //To avoid if token request is coming on whitelist paths
            filterChain.doFilter(request, response);
            return;
        }
        Predicate<String> isBearerToken = authHeader -> authHeader.startsWith("Bearer ");
        Optional.ofNullable(request.getHeader("Authorization"))
                .filter(isBearerToken)
                .map(authHeader -> authHeader.substring(7))
                .ifPresent(jwt -> {
                    try {
                        String username = jwtUtil.extractUsername(jwt);
                        Optional.ofNullable(username)
                                .filter(user -> SecurityContextHolder.getContext().getAuthentication() == null)
                                .ifPresent(user -> {
                                    UserDetails userDetails = userDetailsService().loadUserByUsername(user);
                                    if (jwtUtil.validateToken(jwt, userDetails)) {
                                        log.info("Token Is Valid");
                                        log.info("Authorities" + userDetails.getAuthorities());
                                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                userDetails, jwt, userDetails.getAuthorities());
                                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                        SecurityContextHolder.getContext().setAuthentication(authToken);
                                    } else {
                                        log.info("Token Is not Valid");
                                    }
                                });
                    } catch (Exception e) {
                        log.info(e.getMessage());
                    }
                });
        filterChain.doFilter(request, response);
    }
}
