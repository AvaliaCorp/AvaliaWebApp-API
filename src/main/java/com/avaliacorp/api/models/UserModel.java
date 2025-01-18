package com.avaliacorp.api.models;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("usuario")
public class UserModel {
    
    @Id
    private String id;

    @Column("nome")
    private String name;

    @Column("email")
    private String email;

    @Column("senha")
    private String password;

    @Version
    private Long version;

    public UserModel(String id, String name, String email, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserModel(String name, String email, String password){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserModel(){}

}
