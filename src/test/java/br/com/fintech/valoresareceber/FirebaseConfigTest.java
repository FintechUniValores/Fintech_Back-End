package br.com.fintech.valoresareceber;

import com.google.firebase.FirebaseApp;

import br.com.fintech.valoresareceber.config.FirebaseConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = FirebaseConfig.class)
class FirebaseConfigTest {

    @Autowired
    private ApplicationContext context;

    @Autowired(required = false) 
    private FirebaseApp firebaseApp;

    @Test
    void firebaseAppShouldBeInitialized() {
        // Verifica se o bean FirebaseApp foi criado e injetado
        assertNotNull(firebaseApp, "FirebaseApp não deveria ser nulo, indicando falha na inicialização.");
        System.out.println("FirebaseApp Name: " + firebaseApp.getName());
        System.out.println("FirebaseApp Options Project ID: " + firebaseApp.getOptions().getProjectId());
        // Você pode adicionar mais asserts aqui, como verificar o Project ID se ele estiver definido no seu serviceAccountKey.json
        // assertTrue(!firebaseApp.getOptions().getProjectId().isEmpty(), "Project ID não deveria estar vazio.");
    }

    @Test
    void firebaseConfigBeanShouldExist() {
        FirebaseConfig firebaseConfigBean = context.getBean(FirebaseConfig.class);
        assertNotNull(firebaseConfigBean, "FirebaseConfig bean não foi encontrado no contexto.");
    }
}