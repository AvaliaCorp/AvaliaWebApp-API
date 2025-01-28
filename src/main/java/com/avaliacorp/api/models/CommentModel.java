package com.avaliacorp.api.models;

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

    @Column("likes")
    private Long likes;

    public CommentModel(String text, String authorId, Integer postId, Integer commentId){
        this.text = text;
        this.postId = postId;
        this.authorId = authorId;
        this.commentId = commentId;
    }

}
