package com.avaliacorp.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("comentario")
public class CommentModel {

    @Id
    private Integer id;

    @Column("texto")
    private String text;

    @Column("autor_id")
    private String authorId;

    @Column("avaliacao_id")
    private Integer postId;

    @Column("comentario_id")
    private Integer commentId;

    @Column("criado_em")
    private LocalDateTime createdAt;


    public CommentModel(String text, String authorId, Integer postId, Integer commentId, LocalDateTime createdAt){
        this.text = text;
        this.postId = postId;
        this.authorId = authorId;
        this.commentId = commentId;
        this.createdAt = createdAt;
    }

}
