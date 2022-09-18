package security.attempt1.config.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityFilterConfig {

    private final OAuth2SuccessHandler successHandler;
    private final CorsConfigurationSource configurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(configurationSource);
        http.csrf(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> {
            session.sessionCreationPolicy(STATELESS);
            session.disable();
        });

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        http.oauth2Login().successHandler(successHandler);

        http.authorizeRequests()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .permitAll();
        http.authorizeRequests().antMatchers("/auth/validate").authenticated();
        http.authorizeRequests().anyRequest().permitAll();

        return http.build();
    }
}
