package br.com.fintech.valoresareceber.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import br.com.fintech.valoresareceber.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FirestoreService {

    private Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

    public List<GuideStepDTO> getGuideSteps() throws ExecutionException, InterruptedException {
    ApiFuture<QuerySnapshot> future = getDb().collection("guides").orderBy("order").get(); 
    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    return documents.stream()
            .map(doc -> doc.toObject(GuideStepDTO.class))
            .collect(Collectors.toList());
    }

    public List<FaqsDTO> getFaqs() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getDb().collection("faqs").orderBy("order").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream()
                .map(doc -> doc.toObject(FaqsDTO.class))
                .collect(Collectors.toList());
    }
    
    public List<ProductDTO> getProducts() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getDb().collection("products").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream()
                .map(doc -> doc.toObject(ProductDTO.class))
                .collect(Collectors.toList());
    }

    public List<InstructionalCardDTO> getGovRequirements() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getDb().collection("instructional_cards").orderBy("order").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream()
                .map(doc -> doc.toObject(InstructionalCardDTO.class))
                .collect(Collectors.toList());
    }
}