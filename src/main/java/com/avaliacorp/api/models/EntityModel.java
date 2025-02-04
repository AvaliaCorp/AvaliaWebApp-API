package com.avaliacorp.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;


import lombok.Data;

@Data
public abstract class EntityModel<ID> {

    @Id protected ID id;
    @Column("nome") protected String name;
    @Column("email") protected String email;
    @Column("senha") protected String password;
    @Version protected Long version;

    public EntityModel(ID id, String name, String email, String password, Long version){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.version = version;
    }

    public EntityModel(ID id, String name, String email, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public EntityModel(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public EntityModel(){}
}
