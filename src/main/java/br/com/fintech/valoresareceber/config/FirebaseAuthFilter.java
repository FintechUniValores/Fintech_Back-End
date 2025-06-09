package br.com.fintech.valoresareceber.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException; // Importar FirebaseAuthException
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Para integração com SecurityContext
import org.springframework.security.core.context.SecurityContextHolder; // Para integração com SecurityContext
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList; // Para authorities

@Component
public class FirebaseAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        logger.debug("FirebaseAuthFilter interceptando requisição para: {} {}", method, path);

        // Endpoints que não requerem verificação de token Firebase neste filtro
        // A verificação para /api/oauth2/authorization/govbr é feita no AuthController
        if ((path.equals("/api/login") && "POST".equalsIgnoreCase(method)) ||
                (path.equals("/api/oauth2/authorization/govbr") && "GET".equalsIgnoreCase(method))) {
            logger.debug("Endpoint liberado da verificação de token Firebase neste filtro: {} {}", method, path);
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Header Authorization ausente ou mal formatado para: {} {}", method, path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    "{\"error\": \"Not authorized\", \"message\": \"Authorization header ausente ou mal formatado.\"}");
            response.setContentType("application/json");
            return;
        }

        String idToken = authorizationHeader.substring(7); // Remove "Bearer "

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            logger.info("Token Firebase válido para UID: {} em {} {}", uid, method, path); // "TTToken" removido para
                                                                                           // clareza

            // Opcional, mas recomendado para integração com Spring Security:
            // Se o token for válido, configure o SecurityContext.
            // Isso permite que você use anotações como @PreAuthorize ou acesse o Principal
            // em seus controllers.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    uid, // Pode ser o UID ou o objeto FirebaseToken completo
                    null, // Credenciais (não necessárias aqui, pois o token já foi verificado)
                    new ArrayList<>() // Autoridades (roles), se você as tiver no Firebase ou mapeá-las
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("SecurityContextHolder atualizado com UID: {}", uid);

            filterChain.doFilter(request, response);
        } catch (FirebaseAuthException e) {
            logger.warn("Token Firebase inválido para: {} {}. Erro: {}", method, path, e.getMessage());
            SecurityContextHolder.clearContext(); // Limpar contexto em caso de falha
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter()
                    .write("{\"error\": \"Not authorized\", \"message\": \"Token Firebase inválido ou expirado.\"}");
            response.setContentType("application/json");
            return;
        } catch (Exception e) { // Captura outras exceções inesperadas
            logger.error("Erro inesperado durante a verificação do token Firebase para: {} {}. Erro: {}", method, path,
                    e.getMessage(), e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                    "{\"error\": \"Internal server error\", \"message\": \"Erro inesperado durante a autenticação.\"}");
            response.setContentType("application/json");
            return;
        }
    }
}