package br.com.fintech.valoresareceber.model;


import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    private static final Logger logger = LoggerFactory.getLogger(FirestoreService.class);
    private static final String PLACEHOLDER_DOCUMENT_ID = "_placeholder";

    public Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

    private void ensureCollectionExists(String collectionName) {
        Firestore db = getDb();
        DocumentReference placeholderDocRef = db.collection(collectionName).document(PLACEHOLDER_DOCUMENT_ID);
        try {
            DocumentSnapshot documentSnapshot = placeholderDocRef.get().get();
            if (!documentSnapshot.exists()) {
                Map<String, String> placeholderData = Collections.singletonMap("info", "Este é um documento placeholder para garantir a existência da coleção.");
                placeholderDocRef.set(placeholderData).get();
                logger.info("Coleção '{}' garantida/criada com documento placeholder.", collectionName);
            } else {
                logger.info("Coleção '{}' já existe (documento placeholder encontrado).", collectionName);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao tentar garantir a existência da coleção '{}'", collectionName, e);
        }
    }

    public void initializeFirestoreCollections() {
        logger.info("Inicializando coleções do Firestore...");
        ensureCollectionExists(ProductService.FIRESTORE_COLLECTION_NAME);
        ensureCollectionExists(User.FIRESTORE_COLLECTION_NAME);
        ensureCollectionExists(FAQItem.FIRESTORE_COLLECTION_NAME);
        ensureCollectionExists(GuideStep.FIRESTORE_COLLECTION_NAME);
        logger.info("Inicialização das coleções do Firestore concluída.");
    }

    // --- ProductService Methods ---
    public String saveProductService(ProductService productService) {
        try {
            DocumentReference addedDocRef = getDb().collection(ProductService.FIRESTORE_COLLECTION_NAME).add(productService).get();
            logger.info("ProductService adicionado ao Firestore com ID: {}", addedDocRef.getId());
            return addedDocRef.getId();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao salvar ProductService no Firestore", e);
            throw new RuntimeException("Erro ao salvar ProductService no Firestore", e);
        }
    }

    public ProductService getProductServiceById(String documentId) {
        DocumentReference docRef = getDb().collection(ProductService.FIRESTORE_COLLECTION_NAME).document(documentId);
        try {
            return docRef.get().get().toObject(ProductService.class);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao buscar ProductService no Firestore com ID: {}", documentId, e);
            return null;
        }
    }

    // --- User Methods ---
    public String saveUser(User user) {
        try {
            // Se você usa o UID do Firebase Authentication como ID do documento para User:
            // if (user.getFirebaseUid() != null && !user.getFirebaseUid().isEmpty()) {
            //    getDb().collection(User.FIRESTORE_COLLECTION_NAME).document(user.getFirebaseUid()).set(user).get();
            //    logger.info("User salvo/atualizado no Firestore com UID: {}", user.getFirebaseUid());
            //    return user.getFirebaseUid();
            // } else {
            //    DocumentReference addedDocRef = getDb().collection(User.FIRESTORE_COLLECTION_NAME).add(user).get();
            //    logger.info("User adicionado ao Firestore com ID gerado: {}", addedDocRef.getId());
            //    return addedDocRef.getId();
            // }
            DocumentReference addedDocRef = getDb().collection(User.FIRESTORE_COLLECTION_NAME).add(user).get();
            logger.info("User adicionado ao Firestore com ID: {}", addedDocRef.getId());
            return addedDocRef.getId();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao salvar User no Firestore", e);
            throw new RuntimeException("Erro ao salvar User no Firestore", e);
        }
    }

    public User getUserById(String documentId) {
        DocumentReference docRef = getDb().collection(User.FIRESTORE_COLLECTION_NAME).document(documentId);
        try {
            return docRef.get().get().toObject(User.class);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao buscar User no Firestore com ID: {}", documentId, e);
            return null;
        }
    }

    // --- FAQItem Methods ---
    public String saveFAQItem(FAQItem faqItem) {
        try {
            DocumentReference addedDocRef = getDb().collection(FAQItem.FIRESTORE_COLLECTION_NAME).add(faqItem).get();
            logger.info("FAQItem adicionado ao Firestore com ID: {}", addedDocRef.getId());
            return addedDocRef.getId();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao salvar FAQItem no Firestore", e);
            throw new RuntimeException("Erro ao salvar FAQItem no Firestore", e);
        }
    }

    public FAQItem getFAQItemById(String documentId) {
        DocumentReference docRef = getDb().collection(FAQItem.FIRESTORE_COLLECTION_NAME).document(documentId);
        try {
            return docRef.get().get().toObject(FAQItem.class);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao buscar FAQItem no Firestore com ID: {}", documentId, e);
            return null;
        }
    }

    // --- GuideStep Methods ---
    public String saveGuideStep(GuideStep guideStep) {
        try {
            DocumentReference addedDocRef = getDb().collection(GuideStep.FIRESTORE_COLLECTION_NAME).add(guideStep).get();
            logger.info("GuideStep adicionado ao Firestore com ID: {}", addedDocRef.getId());
            return addedDocRef.getId();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao salvar GuideStep no Firestore", e);
            throw new RuntimeException("Erro ao salvar GuideStep no Firestore", e);
        }
    }

    public GuideStep getGuideStepById(String documentId) {
        DocumentReference docRef = getDb().collection(GuideStep.FIRESTORE_COLLECTION_NAME).document(documentId);
        try {
            return docRef.get().get().toObject(GuideStep.class);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao buscar GuideStep no Firestore com ID: {}", documentId, e);
            return null;
        }
    }
}