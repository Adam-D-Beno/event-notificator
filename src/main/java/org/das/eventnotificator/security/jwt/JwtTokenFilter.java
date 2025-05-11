package org.das.eventnotificator.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
    private final JwtTokenManager jwtTokenManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        LOGGER.info("Execute doFilterInternal in JwtTokenFilter class");

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authorizationHeader.substring(7);
        String userLogin;
        String userRole;
        try {
            userLogin = jwtTokenManager.getLoginFromToken(jwt);
            userRole = jwtTokenManager.getRoleFromToken(jwt);
        } catch (Exception e) {
            LOGGER.error("Error while reading jwt", e);
            filterChain.doFilter(request,response);
            return;
        }
        UserDetails userDetails = User.builder()
                .username(userLogin)
                .authorities(userRole)
                .build();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        addSecurityContextHolder(authenticationToken);
        filterChain.doFilter(request, response);
    }

   private void addSecurityContextHolder(UsernamePasswordAuthenticationToken token) {
        LOGGER.info("Execute method addSecurityContextHolder");
        SecurityContextHolder.getContext().setAuthentication(token);
   }
}