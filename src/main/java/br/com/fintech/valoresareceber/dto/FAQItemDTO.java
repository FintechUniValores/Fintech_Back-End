package br.com.fintech.valoresareceber.dto;

import lombok.Data;

@Data
public class FAQItemDTO {
    private Long id;
    private String pergunta;
    private String resposta;
}