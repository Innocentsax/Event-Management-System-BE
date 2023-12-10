package dev.Innocent.EventManagementSystem.security;


import dev.Innocent.EventManagementSystem.configuration.JwtFilterConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.http.HttpMethod.POST;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityFilterChainConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilterConfiguration filterConfiguration;
    private final CorsConfigurationSource corsConfigurationSource;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer->
                        configurer
                                .requestMatchers("/api/v1","/api/v1/auth/**","/password/**", "/events/view-event/**",
                                        "/v3/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/reset_password", "/api/v1/event/UserEvent").hasRole("USER")
                                .requestMatchers(POST, "/api/v1/auth/logout").hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/event/publish/**" ).hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/api/admin/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/v1/event/save_event","/api/v1/event/UserEvent" ).hasRole("USER")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/event/edit/**" ).hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/event/delete/**" ).hasRole("USER")
                                .requestMatchers(HttpMethod.POST,"/api/v1/auth/signup").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/auth/login", "api/v1/user-profile/create").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/auth/forgot-password").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/auth/verify-password-token/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/auth/register/verify/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/ticket/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/user-profile/editProfile").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/user-profile/create").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/event/ticket_types","/api/v1/event/all_event_category"
                                        ,"/api/v1/event/search/**", "/api/v1/event/view/**",
                                        "/api/v1/event/all_tickets_category").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/event/ticket_types","/api/v1/event/all_event_category","/api/v1/event/search/**", "/api/v1/event/all_tickets_category").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/event/all_tickets_category_banks").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/banks").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/banks/verify","/api/v1/transaction/bank/list","api/seed/banks").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/transfer").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/transaction/transfer/recipient", "/api/v1/transaction/initializeTransaction").permitAll()


                ).sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(filterConfiguration, UsernamePasswordAuthenticationFilter.class);

        http.csrf(csrf->csrf.disable());
        http.cors().configurationSource(corsConfigurationSource);
        return http.build();
    }
}


