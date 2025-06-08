package br.com.fintech.valoresareceber.config;

import br.com.fintech.valoresareceber.model.FirestoreService; // Certifique-se que o import para FirestoreService está correto
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupRunner.class);
    private final FirestoreService firestoreService;
    private final FirebaseConfig firebaseConfig; // Para garantir que o Firebase seja inicializado antes

    // Injeção de dependências via construtor
    public ApplicationStartupRunner(FirestoreService firestoreService, FirebaseConfig firebaseConfig) {
        this.firestoreService = firestoreService;
        this.firebaseConfig = firebaseConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        // Garante que o FirebaseApp foi inicializado antes de tentar usar o FirestoreService
        // firebaseApp() é o método @Bean da sua FirebaseConfig que retorna FirebaseApp
        if (firebaseConfig.firebaseApp() != null) {
            logger.info("Firebase App inicializado, procedendo com a inicialização das coleções do Firestore.");
            // AQUI é onde você chama o método para inicializar as coleções
            firestoreService.initializeFirestoreCollections();
        } else {
            logger.error("Firebase App não foi inicializado. Não foi possível inicializar as coleções do Firestore.");
        }
    }
}