package br.com.fintech.valoresareceber.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public static final String FIRESTORE_COLLECTION_NAME = "usuarios";

    // private String firestoreDocumentId; // Opcional: para armazenar o ID do documento do Firestore
    // private String firebaseUid; // UID do Firebase Authentication, frequentemente usado como ID do documento
    private String cpf;
    private String nivelGovBr;
    private boolean twoFactorEnabled;
    private String email; // Adicionando email, pois é comum para usuários

    // Se você estiver usando o UID do Firebase Authentication como ID do documento,
    // você pode querer um construtor ou setter para ele.
}