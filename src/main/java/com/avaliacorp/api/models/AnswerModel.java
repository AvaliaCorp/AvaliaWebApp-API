package com.avaliacorp.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("resposta_empresa")
public class AnswerModel {

    @Id
    private Integer id;

    @Column("resposta")
    private String text;

    @Column("empresa_cnpj")
    private String cnpj;

    @Column("avaliacao_id")
    private Integer postId;

    @Column("data_resposta")
    private LocalDateTime createdAt;

    public AnswerModel(Integer id, Integer postId, String text, String cnpj, LocalDateTime createdAt){
        this.id = id;
        this.text = text;
        this.cnpj = cnpj;
        this.postId = postId;
        this.createdAt = createdAt;
    }
    
}
