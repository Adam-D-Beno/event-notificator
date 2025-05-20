package org.das.eventnotificator.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.security.CustomUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
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
        Long userId;
        try {
            userLogin = jwtTokenManager.getLoginFromToken(jwt);
            userRole = jwtTokenManager.getRoleFromToken(jwt);
            userId = jwtTokenManager.getIdFromToken(jwt);
        } catch (Exception e) {
            LOGGER.error("Error while reading jwt", e);
            filterChain.doFilter(request,response);
            return;
        }
        UserDetails customUserDetails = CustomUserDetail.builder()
                .id(userId)
                .username(userLogin)
                .authorities(Collections.singleton(new SimpleGrantedAuthority(userRole)))
                .build();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );

        addSecurityContextHolder(authenticationToken);
        filterChain.doFilter(request, response);
    }

   private void addSecurityContextHolder(UsernamePasswordAuthenticationToken token) {
        LOGGER.info("Execute method addSecurityContextHolder");
        SecurityContextHolder.getContext().setAuthentication(token);
   }
}