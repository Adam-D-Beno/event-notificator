package org.das.eventnotificator.security.jwt;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.exeption.CustomAccessDeniedHandler;
import org.das.eventnotificator.exeption.CustomAuthenticationEntryPoint;
import org.das.eventnotificator.model.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(true).ignoring()
                .requestMatchers("/css/**",
                        "/js/**",
                        "/img/**",
                        "/lib/**",
                        "/favicon.ico",
                        "/swagger-ui/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v3/api-docs/swagger-config",
                        "/openapi.yaml"
                );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(matcherRegistry -> {
                    matcherRegistry
                            .requestMatchers(HttpMethod.GET, "/notifications")
                            .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                            .requestMatchers(HttpMethod.GET, "/notifications/test")
                            .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                            .requestMatchers(HttpMethod.POST, "/notifications")
                            .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                            .anyRequest()
                            .authenticated();
                })
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(authenticationEntryPoint);
                    exception.accessDeniedHandler(customAccessDeniedHandler);
                })
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
