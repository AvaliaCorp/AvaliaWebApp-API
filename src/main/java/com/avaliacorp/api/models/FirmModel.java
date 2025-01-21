package com.avaliacorp.api.models;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table("empresa")
public class FirmModel extends EntityModel<String> {

    @Column("CNPJ")
    private String CNPJ;

    public FirmModel(String id, String name, String email, String password, String CNPJ){
        super(id, name, email, password);
        this.CNPJ = CNPJ;
    }

    public FirmModel(String name, String email, String password, String CNPJ){
        super(name, email, password);
        this.CNPJ = CNPJ;
    }

    public FirmModel(){}
}
