//package app.project_fin_d_etude.config;
//
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
//import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
//import org.springframework.security.core.session.SessionRegistryImpl;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
//import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true) // Active la sécurité au niveau des méthodes
//public class KeycloakSecurityConfig {
//
//    /**Fournit le fournisseur d'authentification Keycloak.*/
//    @Bean
//    public KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
//        KeycloakAuthenticationProvider provider = new KeycloakAuthenticationProvider();
//        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper()); // Mappe les rôles Keycloak aux rôles Spring Security
//        return provider;
//    }
//
//    @Bean
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
//    }
//
//    /** Configure les règles de sécurité HTTP. Définit les points d'accès publics et protégés */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Désactive CSRF pour simplifier (à revoir en production)
//                .sessionManagement(session -> session
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Vaadin nécessite des sessions
//                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
//                )
//                .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/VAADIN/**", "/HEARTBEAT/**", "/UIDL/**", "/PUSH/**").permitAll() // Ressources Vaadin
//                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Ressources statiques
//                .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
//                )
//                .oauth2Login(oauth2 -> oauth2
//                .loginPage("/oauth2/authorization/keycloak") // Redirige vers Keycloak pour l'authentification
//                .defaultSuccessUrl("/", true)
//                );
//
//        return http.build();
//    }
//
//    /** Empêche l'adaptateur Keycloak de charger la configuration depuis keycloak.json. La configuration est lue depuis application.properties.
//     */
//    @Bean
//    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }
//}
