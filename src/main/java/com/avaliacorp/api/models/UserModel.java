package com.avaliacorp.api.models;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table("usuario")
public class UserModel extends EntityModel<String> {

    @Column("tipo")
    private String role;

    public UserModel(String id, String name, String email, String password, String role){
        super(id, name, email, password);
        this.role = role;
    }

    public UserModel(String name, String email, String password, String role){
        super(name, email, password);
        this.role = role;
    }

    public UserModel(){}

}
