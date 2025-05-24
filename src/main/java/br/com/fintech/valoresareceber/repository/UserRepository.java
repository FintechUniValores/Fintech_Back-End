package br.com.fintech.valoresareceber.repository;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;
import br.com.fintech.valoresareceber.model.User;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {

    private final Firestore db;

    public UserRepository() {
        this.db = FirestoreClient.getFirestore();
    }

    public void saveUser(User user) throws ExecutionException, InterruptedException {
        db.collection("users").document(user.getCpf()).set(user).get();
    }

    public User getUserByCpf(String cpf) throws ExecutionException, InterruptedException {
        return db.collection("users").document(cpf).get().get().toObject(User.class);
    }
    // ... outros métodos CRUD
}
