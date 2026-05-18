package com.RestroPlusWebsite_20.Configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                /*
                 ============================================
                            DISABLE CSRF
                 ============================================
                */
                .csrf(csrf -> csrf.disable())

                /*
                 ============================================
                            AUTHORIZE REQUESTS
                 ============================================
                */
                .authorizeHttpRequests(auth -> auth

                        /*
                         ====================================
                                PUBLIC ROUTES
                         ====================================
                        */
                        .requestMatchers(

                                "/",
                                "/index",

                                // PUBLIC WEB PAGES
                                "/web/**",

                                // AUTH PAGES
                                "/auth/**",

                                // LOGOUT
                                "/logout",

                                // STATIC FILES
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**"

                        ).permitAll()

                        /*
                         ====================================
                                USER DASHBOARD
                         ====================================
                        */
                        .requestMatchers(
                                "/dashboard/user"
                        )
                        .hasAnyRole("USER", "ADMIN")

                        /*
                         ====================================
                                ADMIN ROUTES
                         ====================================
                        */
                        .requestMatchers(

                                "/dashboard/admin",

                                "/set-manu",

                                "/upload-category-image/**",

                                "/category/delete/**",

                                "/category/details/**",

                                "/category/details/save",

                                "/upload-menu-card",

                                "/save-menu-card"

                        )
                        .hasRole("ADMIN")

                        /*
                         ====================================
                                DRIVER ROUTES
                         ====================================
                        */
                        .requestMatchers(
                                "/dashboard/drivers"
                        )
                        .hasRole("DRIVER")

                        /*
                         ====================================
                                EVERYTHING ELSE
                         ====================================
                        */
                        .anyRequest()
                        .authenticated()
                )

                /*
                 ============================================
                        DISABLE SPRING LOGIN PAGE
                 ============================================
                */
                .formLogin(form -> form.disable())

                /*
                 ============================================
                            EXCEPTION HANDLING
                 ============================================
                */
                .exceptionHandling(ex -> ex

                        // NOT LOGGED IN
                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    redirectToLogin(
                                            request,
                                            response
                                    );
                                }
                        )

                        // SESSION EXPIRED / WRONG ROLE
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    redirectToLogin(
                                            request,
                                            response
                                    );
                                }
                        )
                )

                /*
                 ============================================
                                LOGOUT
                 ============================================
                */
                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/web/home")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                )

                /*
                 ============================================
                            SESSION MANAGEMENT
                 ============================================
                */
                .sessionManagement(sess -> sess

                        .maximumSessions(1)
                );

        return http.build();
    }

    /*
     =========================================================
                        REDIRECT METHOD
     =========================================================
    */
    private void redirectToLogin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws java.io.IOException {

        String uri = request.getRequestURI();

        /*
         ============================================
                        ADMIN ROUTES
         ============================================
        */
        if (

                uri.startsWith("/dashboard/admin")

                        ||

                        uri.startsWith("/set-manu")

                        ||

                        uri.startsWith("/category")

                        ||

                        uri.startsWith("/upload")

        ) {

            response.sendRedirect("/auth/restaurant");
        }

        /*
         ============================================
                        USER ROUTES
         ============================================
        */
        else if (

                uri.startsWith("/dashboard/user")

        ) {

            response.sendRedirect("/auth/customer");
        }

        /*
         ============================================
                        DRIVER ROUTES
         ============================================
        */
        else if (

                uri.startsWith("/dashboard/drivers")

        ) {

            response.sendRedirect("/auth/partner");
        }

        /*
         ============================================
                            DEFAULT
         ============================================
        */
        else {

            response.sendRedirect("/web/home");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}