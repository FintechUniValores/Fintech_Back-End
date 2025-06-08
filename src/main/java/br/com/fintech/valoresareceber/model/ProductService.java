package br.com.fintech.valoresareceber.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductService {

    public static final String FIRESTORE_COLLECTION_NAME = "produtosServicos";

    // private String id; // Opcional: para armazenar o ID do documento do Firestore
    private String nome;
    private String slogan;
    private String descricaoCompleta;
    private String iconeUrl;
    private String linkExterno;

}