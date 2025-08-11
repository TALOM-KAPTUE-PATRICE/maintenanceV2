package com.kaptue.dev.maintenance.config; // Changement de package

import java.util.Arrays; // Nouvelle classe pour gérer les erreurs 401

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Importer

import com.kaptue.dev.maintenance.security.JwtAuthenticationEntryPoint; // Importer
import com.kaptue.dev.maintenance.security.JwtRequestFilter; // Importer

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    // ▼▼▼ INJECTEZ LA PROPRIÉTÉ ▼▼▼
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // Pour gérer les erreurs d'authentification

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour une API stateless
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Utiliser la configuration CORS bean
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Gérer les erreurs 401
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Endpoints d'authentification (login, register)
                .requestMatchers("/profile-images/**", "/b_commandes_client/**" ,"/static/**", "/images/**", "/favicon.png").permitAll() // Contenu public
                .requestMatchers("/ws/**").permitAll() // Pour WebSocket si utilisé
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permettre les requêtes OPTIONS pour CORS

                // La sécurité des endpoints spécifiques (Tickets, Devis, etc.)
                // sera gérée avec @PreAuthorize directement dans les contrôleurs.
                // Cela offre plus de flexibilité pour la logique basée sur les postes.
                // Exemples de règles globales si vous en aviez besoin (mais @PreAuthorize est mieux) :
                // .requestMatchers("/api/users/**").hasAuthority(Permission.USER_MANAGE)

                .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(","))); // Votre frontend Angular
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Ou spécifiez les headers nécessaires
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer cette configuration à toutes les routes
        return source;
    }
}
