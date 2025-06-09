package br.com.fintech.valoresareceber.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord.CreateRequest;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // Certifique-se que esta anotação está presente
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository; // Importação correta
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // 1. Declaração do campo
    private final ClientRegistrationRepository clientRegistrationRepository;

    // 2. Injeção via construtor com @Autowired
    @Autowired
    public AuthController(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository; // Inicialização do campo
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody(required = false) Map<String, String> payload) {
        // ... seu código de login ...
        logger.info("POST /api/login - Payload recebido: {}", payload);

        if (payload == null) {
            logger.warn("/api/login - Payload nulo.");
            return ResponseEntity.badRequest().body("Body obrigatório com email e senha.");
        }
        String email = payload.get("email");
        String senha = payload.get("senha");
        logger.debug("/api/login - Email: '{}', Senha presente: {}", email, senha != null && !senha.isEmpty());

        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            logger.warn("/api/login - Validação falhou: Email ou senha ausente/vazio. Email: '{}', Senha vazia: {}", email, (senha == null || senha.trim().isEmpty()));
            return ResponseEntity.badRequest().body("Email e senha são obrigatórios e não podem ser vazios.");
        }

        logger.info("/api/login - Validação de email e senha passou. Tentando autenticar/criar usuário.");
        try {
            UserRecord userRecord;
            try {
                userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
                logger.info("/api/login - Usuário encontrado com email: {}", email);
            } catch (FirebaseAuthException e) {
                logger.warn("/api/login - Usuário não encontrado com email: {}. Tentando criar.", email);
                CreateRequest request = new CreateRequest().setEmail(email).setPassword(senha);
                userRecord = FirebaseAuth.getInstance().createUser(request);
                logger.info("/api/login - Usuário criado com email: {}", email);
            }
            String customToken = FirebaseAuth.getInstance().createCustomToken(userRecord.getUid());
            logger.info("/api/login - Custom token gerado para UID: {}", userRecord.getUid());
            return ResponseEntity.ok(Map.of("token", customToken));
        } catch (Exception e) {
            logger.error("/api/login - Erro ao autenticar/criar usuário: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autenticar/criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/api/oauth2/authorization/govbr")
    public ResponseEntity<?> getGovBrAuthorizationUrl(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            HttpServletRequest httpRequest) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Tentativa de acesso a /api/oauth2/authorization/govbr sem token Bearer ou header Authorization ausente.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token de autorização ausente ou mal formatado."));
        }

        String firebaseIdToken = authorizationHeader.substring(7);
        FirebaseToken decodedToken;
        UserRecord userRecordAuth;
        
        try {
            
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            logger.info("Token Firebase ID verificado com sucesso para UID: {}. Prosseguindo com a autorização Gov.br.", uid);

            userRecordAuth = FirebaseAuth.getInstance().getUser(uid);
            if (userRecordAuth.isDisabled()) {
                logger.warn("Usuário com UID: {} está desabilitado no Firebase.", uid);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Conta de usuário desabilitada."));
            }
            logger.info("Conta do usuário com UID: {} verificada e ativa no Firebase.", uid);

        } catch (FirebaseAuthException e) {
            logger.warn("Falha ao verificar token Firebase ID ou buscar usuário: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token de autorização inválido ou erro ao buscar usuário.", "details", e.getMessage()));
        }

        // A linha 112 (aproximadamente) estaria aqui ou logo abaixo, usando this.clientRegistrationRepository
        ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId("govbr"); // Uso do campo
        if (clientRegistration == null) {
            logger.error("ClientRegistration 'govbr' não encontrado. Verifique as configurações OAuth2.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Configuração OAuth2 para Gov.br não encontrada no backend."));
        }

        String state = generateRandomString(40);

        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode()
                .clientId(clientRegistration.getClientId())
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .redirectUri(clientRegistration.getRedirectUri())
                .scopes(clientRegistration.getScopes())
                .state(state);

        OAuth2AuthorizationRequest authorizationRequest = builder.build();
        String authorizationUrl = authorizationRequest.getAuthorizationRequestUri();

        logger.info("Construída URL de autorização Gov.br: {}", authorizationUrl);
        return ResponseEntity.ok(Map.of("authorizationUrl", authorizationUrl, "state", state));
    }

    private String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}