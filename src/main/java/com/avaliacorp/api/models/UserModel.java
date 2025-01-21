package com.avaliacorp.api.models;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table("usuario")
public class UserModel extends EntityModel<String> {

    public UserModel(String id, String name, String email, String password){
        super(id, name, email, password);
    }

    public UserModel(String name, String email, String password){
        super(name, email, password);
        this.id = UUID.randomUUID().toString();
    }

    public UserModel(){}

}
