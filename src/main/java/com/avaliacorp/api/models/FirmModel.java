package com.avaliacorp.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("empresa")
public class FirmModel {

    @Id
    private String id;

    @Column("nome")
    private String name;

    @Column("email")
    private String email;

    @Column("senha")
    private String password;

    @Column("CNPJ")
    private String CNPJ;

    @Version
    private Long version;

    public FirmModel(String id, String name, String email, String password, String CNPJ){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.CNPJ = CNPJ;
    }

    public FirmModel(String name, String email, String password, String CNPJ){
        this.name = name;
        this.email = email;
        this.password = password;
        this.CNPJ = CNPJ;
    }

    public FirmModel(){}
}
