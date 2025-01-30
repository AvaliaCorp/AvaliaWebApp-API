package com.avaliacorp.api.models;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("likes")
public class LikeModel {

    @Column("usuario_id")
    private String userId;

    @Column("avaliacao_id")
    private Integer postId;

    @Column("comentario_id")
    private Integer commentId;

    /**
     * Like Constructor
     * @param userId
     * @param postId
     * @param commentId
     */
    public LikeModel(String userId, Integer postId, Integer commentId){
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
    }

    public LikeModel(){}

}
