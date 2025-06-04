package br.com.fintech.valoresareceber.dto;

import lombok.Data;

@Data
public class ProductServiceDTO {
    private Long id;
    private String nome;
    private String slogan;
    private String descricaoCompleta;
    private String iconeUrl;
    private String linkExterno;
}