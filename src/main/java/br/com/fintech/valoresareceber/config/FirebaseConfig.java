package br.com.fintech.valoresareceber.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException; // Added import
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger; // Adicionar import para Logger
import org.slf4j.LoggerFactory; // Adicionar import para LoggerFactory
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value; // Corrected import
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class); // Adicionar Logger

    @Value("${firebase.service-account.path:classpath:serviceAccountKey.json}")
    private String serviceAccountPath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (serviceAccountPath == null || serviceAccountPath.trim().isEmpty()) {
            String errorMessage = "Firebase service account path ('firebase.service-account.path') is not configured or is empty.";
            logger.error(errorMessage);
            throw new IOException(errorMessage);
        }

        InputStream serviceAccountStream = null;
        try {
            logger.info("Tentando carregar serviceAccountKey.json do caminho: {}", serviceAccountPath);
            serviceAccountStream = new FileInputStream(serviceAccountPath);
            // Se você estivesse usando ClassPathResource:
            // logger.info("Tentando carregar serviceAccountKey.json do classpath: serviceAccountKey.json");
            // serviceAccountStream = new ClassPathResource("serviceAccountKey.json").getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                logger.info("Inicializando FirebaseApp...");
                return FirebaseApp.initializeApp(options);
            } else {
                logger.info("FirebaseApp já inicializado, retornando instância existente.");
                return FirebaseApp.getInstance();
            }
        } catch (FileNotFoundException e) { // Specific catch for file not found
            logger.error("ERRO CRÍTICO: Arquivo de chave de conta de serviço do Firebase NÃO ENCONTRADO no caminho: {}. Detalhes: {}", serviceAccountPath, e.getMessage(), e);
            throw new IOException("Falha ao inicializar Firebase Admin SDK: Arquivo de chave não encontrado em '" + serviceAccountPath + "'", e);
        } catch (IOException e) { // Catch other IOExceptions (e.g., reading credentials, other stream issues)
            logger.error("ERRO CRÍTICO DE I/O AO INICIALIZAR FIREBASE (caminho: {}): {}. ", serviceAccountPath, e.getMessage(), e);
            throw new IOException("Falha ao inicializar Firebase Admin SDK devido a um erro de I/O processando o arquivo: " + serviceAccountPath, e);
        } catch (Exception e) { // Catch any other unexpected exceptions during initialization
            logger.error("ERRO CRÍTICO INESPERADO AO INICIALIZAR FIREBASE (caminho: {}): {}. ", serviceAccountPath, e.getMessage(), e);
            throw new IOException("Falha ao inicializar Firebase Admin SDK devido a um erro inesperado processando o arquivo: " + serviceAccountPath, e);
        } finally {
            if (serviceAccountStream != null) {
                try {
                    serviceAccountStream.close();
                } catch (IOException e) {
                    logger.warn("Falha ao fechar serviceAccountStream", e);
                }
            }
        }
    }
}