package br.com.fintech.valoresareceber.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe de configuração responsável por inicializar o Firebase Admin SDK
 * na inicialização da aplicação Spring Boot.
 */
@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @PostConstruct
    public void initializeFirebase() {
        try {
            // O nome do seu arquivo de chave de conta de serviço.
            // Coloque este arquivo dentro da pasta 'src/main/resources'.
            String serviceAccountFile = "serviceAccountKey.json";

            // Usamos ClassPathResource para carregar o arquivo do classpath da aplicação.
            // Isso funciona bem tanto em desenvolvimento quanto em produção (dentro de um arquivo JAR).
            ClassPathResource resource = new ClassPathResource(serviceAccountFile);
            InputStream serviceAccountStream = resource.getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .build();

            // Evita erro de reinicialização (especialmente útil em desenvolvimento com hot reload)
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase App foi inicializado com sucesso!");
            }

        } catch (IOException e) {
            logger.error("Erro ao inicializar o Firebase App", e);
            // Lançar uma exceção aqui pode fazer a aplicação falhar na inicialização,
            // o que é um comportamento desejável se o Firebase for crítico.
            throw new RuntimeException(e);
        }
    }
}