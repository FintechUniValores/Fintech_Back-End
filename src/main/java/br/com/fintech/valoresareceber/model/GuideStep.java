package br.com.fintech.valoresareceber.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideStep {

    public static final String FIRESTORE_COLLECTION_NAME = "guidePassos";

    // private String id; // Opcional: para armazenar o ID do documento do Firestore
    private String titulo;
    private String descricao;
    private int ordem;

}