package br.com.fintech.valoresareceber.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String nivelGovBr;

    public UserDTO() {}

    public UserDTO(Long id, String nivelGovBr) {
        this.id = id;
        this.nivelGovBr = nivelGovBr;
    }
}