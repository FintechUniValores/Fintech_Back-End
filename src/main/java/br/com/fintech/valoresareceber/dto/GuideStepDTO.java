package br.com.fintech.valoresareceber.dto;

import lombok.Data;

@Data
public class GuideStepDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private int ordem;
}