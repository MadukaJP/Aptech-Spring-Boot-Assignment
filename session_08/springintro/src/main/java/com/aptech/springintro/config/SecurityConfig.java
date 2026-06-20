// ============================================================
// SNIPPET 02: SecurityConfig.java
// ============================================================
// THE ONE FILE THAT CONTROLS YOUR ENTIRE SECURITY SETUP.
//
// It has THREE jobs:
// 1. Define HOW passwords are stored (BCrypt encoder)
// 2. Define WHO the users are (UserDetailsService)
// 3. Define WHAT each user can access (SecurityFilterChain)
// ============================================================

package com.aptech.springintro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// @Configuration → This class provides Spring Beans (configuration)
// @EnableMethodSecurity → Unlocks @PreAuthorize, @PostAuthorize annotations
@EnableMethodSecurity
public class SecurityConfig {


    // ════════════════════════════════════════════════════════
    // BEAN 1: Password Encoder
    // ════════════════════════════════════════════════════════
    // BCrypt is a one-way hashing algorithm.
    // "student123" → "$2a$10$N9qo8uLOickgx2ZMRZoMye..." (not reversible)
    // When a user logs in, Spring BCrypt-hashes their input
    // and COMPARES the hashes. It never stores or compares plain text.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // The "10" in $2a$10$ means "cost factor 10"
        // Higher cost = slower hashing = harder to brute-force
        // BCryptPasswordEncoder() defaults to cost factor 10
    }


    // ════════════════════════════════════════════════════════
    // BEAN 2: User Store (In-Memory)
    // ════════════════════════════════════════════════════════
    // This is a SIMPLE in-memory user store.
    // Good for: learning, testing, demos
    // NOT for: production (users are lost when app restarts)
    // Session 8 will replace this with a real database.

    @Bean
    public UserDetailsService userDetailsService() {

        // User.builder() gives us a clean way to create UserDetails objects
        var alice = User.builder()
                .username("alice")
                .password(passwordEncoder().encode("alice123"))
                // ↑ BCrypt-encoded. Plain "alice123" is NEVER stored.
                .roles("STUDENT")
                // Spring automatically adds "ROLE_" prefix:
                // roles("STUDENT") → stores "ROLE_STUDENT"
                .build();

        var bob = User.builder()
                .username("bob")
                .password(passwordEncoder().encode("bob456"))
                .roles("INSTRUCTOR")
                .build();

        var admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin789"))
                .roles("ADMIN")
                .build();

        // InMemoryUserDetailsManager stores these users in RAM
        return new InMemoryUserDetailsManager(alice, bob, admin);
    }


    // ════════════════════════════════════════════════════════
    // BEAN 3: Security Filter Chain
    // ════════════════════════════════════════════════════════
    // This is your RULES ENGINE.
    // You describe: "For URL X, user must have ROLE Y."
    // Spring enforces these rules automatically via the filter chain.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

            // ── URL ACCESS RULES ──────────────────────────────
            .authorizeHttpRequests(auth -> auth

                // Rule 1: Login page & static resources — public (no login needed)
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()

                // Rule 2: Home page, student list, courses — any logged-in user can view
                .requestMatchers("/", "/students", "/courses", "/courses/**").authenticated()

                // Rule 3: Add student — only INSTRUCTOR or ADMIN
                .requestMatchers("/students/add").hasAnyRole("INSTRUCTOR", "ADMIN")

                // Rule 4: Edit student — only INSTRUCTOR or ADMIN
                .requestMatchers("/students/edit/**").hasAnyRole("INSTRUCTOR", "ADMIN")

                // Rule 5: Delete student — ADMIN only
                // "/**" means: /students/delete/1, /students/delete/99, etc.
                .requestMatchers("/students/delete/**").hasRole("ADMIN")

                // Rule 6: Admin area — ADMIN only
                .requestMatchers("/admin/**").hasRole("ADMIN")

                //By Pass 
                .requestMatchers("/api/**").permitAll()

                // Rule 7: Anything else not mentioned → must be logged in
                .anyRequest().authenticated()

                // ⚠️ RULE ORDER MATTERS! More specific rules MUST come before general ones.
                // .anyRequest() must ALWAYS be last.
            )

            // ── LOGIN CONFIGURATION ──────────────────────────
            .formLogin(form -> form
                .loginPage("/login")            // Our custom login page URL
                .loginProcessingUrl("/login")   // Spring handles the POST here
                //  ↑ These two being the same URL is fine.
                //    GET /login → shows the login page (your controller)
                //    POST /login → Spring Security processes the credentials (automatic)
                .defaultSuccessUrl("/students", true)
                //  ↑ true = always redirect to /students after login
                //    false = go to the page they originally tried to access
                .failureUrl("/login?error=true") // Failed login → append ?error=true
                .permitAll()                     // Login page must be public!
            )

            // ── LOGOUT CONFIGURATION ─────────────────────────
            .logout(logout -> logout
                .logoutUrl("/logout")               // POST to this URL to log out
                .logoutSuccessUrl("/login?logout=true") // After logout, go here
                .invalidateHttpSession(true)        // Destroy the server-side session
                .deleteCookies("JSESSIONID")        // Remove the session cookie from browser
                .permitAll()
            )

            // ── REMEMBER ME ──────────────────────────────────
            .rememberMe(remember -> remember
                .key("aptechSecretKey")
                .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 days
            )

            // ── CSRF CONFIGURATION ───────────────────────────
            // CSRF (Cross-Site Request Forgery) protection is ENABLED by default.
            // Thymeleaf's th:action="@{/url}" adds the CSRF token automatically.
            // If you use REST APIs (non-Thymeleaf), you may need to disable CSRF:
            // .csrf(csrf -> csrf.disable())
            ;

        return http.build();
    }
}