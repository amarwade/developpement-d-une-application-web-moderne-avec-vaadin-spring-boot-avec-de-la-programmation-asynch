package app.project_fin_d_etude.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class KeycloakSecurityConfig {

    @Bean
    public KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        KeycloakAuthenticationProvider provider = new KeycloakAuthenticationProvider();
        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        return provider;
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                )
                .authorizeHttpRequests(auth -> auth
                // 💡 Autoriser ressources Vaadin + statiques
                .requestMatchers(
                        "/VAADIN/**", "/HEARTBEAT/**", "/UIDL/**", "/PUSH/**",
                        "/css/**", "/js/**", "/images/**",
                        "/", "/about", "/contact"
                ).permitAll()
                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/keycloak")
                .defaultSuccessUrl("/articles", true)
                )
                .logout(logout -> logout
                .logoutSuccessUrl("/?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}
