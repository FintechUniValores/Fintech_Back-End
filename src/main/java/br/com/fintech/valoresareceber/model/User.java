package br.com.fintech.valoresareceber.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    private String nivelGovBr;
    private boolean twoFactorEnabled; 

  

public User() {
    
}

       
    public User(String cpf, String nivelGovBr, boolean twoFactorEnabled) {
        this.cpf = cpf;
        this.nivelGovBr = nivelGovBr;
        this.twoFactorEnabled = twoFactorEnabled;
    }
}