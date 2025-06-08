package br.com.fintech.valoresareceber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService; // Não é mais necessário se OAuth2 está desabilitado
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilitar CSRF se sua API for stateless e você usar tokens (como Firebase ID tokens)
            // Se você tiver partes do seu app que usam sessões e formulários web, configure CSRF adequadamente.
            .csrf(csrf -> csrf.disable())
            // Configurar o gerenciamento de sessão para STATELESS, já que estamos usando tokens Firebase
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                // Permitir acesso não autenticado (pelo Spring Security) a estes endpoints específicos
                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/oauth2/authorization/govbr").permitAll()
                // Adicione outros endpoints públicos aqui, se houver
                // .requestMatchers("/public/**").permitAll()

                // Todos os outros endpoints devem ser autenticados
                // Se você tiver um filtro Firebase, ele cuidará da autenticação para os demais.
                // Se não, você precisará definir como os outros endpoints são protegidos.
                .anyRequest().authenticated() // Exemplo: requer autenticação para qualquer outra requisição
            );
            // Se você tiver um filtro de autenticação Firebase para proteger outros endpoints:
            // http.addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter.class);

            // Se você estiver usando o fluxo OAuth2/OIDC padrão do Spring Security para o callback do Gov.br
            // (ex: /login/oauth2/code/govbr), ele já tem suas próprias configurações de segurança.
            // Se você estiver lidando com o callback manualmente, precisará permitir acesso a ele também.

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:19006")); // Adicione as URLs do seu frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}