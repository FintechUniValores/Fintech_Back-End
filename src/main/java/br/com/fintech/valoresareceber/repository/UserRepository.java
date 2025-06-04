package br.com.fintech.valoresareceber.repository;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired; // Import Autowired
import org.springframework.stereotype.Repository;
import br.com.fintech.valoresareceber.model.User;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {

    private final Firestore db;

    @Autowired // Add Autowired for clarity, though often optional for single constructors
    public UserRepository(FirebaseApp firebaseApp) {
        this.db = FirestoreClient.getFirestore(firebaseApp);
    }

//   public void saveUser(User user) throws ExecutionException, InterruptedException {
//       db.collection("users").document(user.getCpf()).set(user).get();
//   }

    public User getUserByCpf(String cpf) throws ExecutionException, InterruptedException {
        var docSnap = db.collection("users").document(cpf).get().get();
        if (docSnap.exists()) { 
            return docSnap.toObject(User.class);
        }
        return null;
    }
}
