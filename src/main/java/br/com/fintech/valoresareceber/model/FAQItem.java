package br.com.fintech.valoresareceber.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FAQItem {

    public static final String FIRESTORE_COLLECTION_NAME = "faqItens";

    // private String id; // Opcional: para armazenar o ID do documento do Firestore
    private String pergunta;
    private String resposta;

}