package com.avaliacorp.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("avaliacao")
public class PostModel {

    @Id
    private Integer id;

    @Column("titulo")
    private String title;

    @Column("empresa_cnpj")
    private String fkCNPJ;

    @Column("usuario_id")
    private String authorId;

    @Column("nota")
    private String grade;

    @Column("texto")
    private String text;

    @Column("status")
    private Boolean status;

    @Column("criado_em")
    private LocalDateTime createdAt;

    public PostModel(Integer id, String title, String fkCNPJ, String authorId, String grade, String text, Boolean status){
        this.id = id;
        this.title = title;
        this.fkCNPJ = fkCNPJ;
        this.authorId = authorId;
        this.createdAt = LocalDateTime.now();
        this.status = status;
        this.grade = grade;
        this.text = text;
    }

}
