package com.RestroPlusWebsite_20.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Public + Auth pages
                        .requestMatchers(
                                "/",
                                "/index",
                                "/home",
                                "/about",
                                "/contact",

                                "/web/**",
                                "/auth/**",
                                "/user-login",
                                "/register-user",

                                // static resources
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/web/**",
                                "/webjars/**"
                        ).permitAll()

                        // Dashboard is allowed — we protect it manually by session
                        .requestMatchers("/dashboard/**").permitAll()

                        // Nothing else should be protected by Spring Security
                        .anyRequest().permitAll()
                )

                // keep SAME session after login redirect
                .sessionManagement(sess ->
                        sess.sessionFixation(fix -> fix.none())
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
