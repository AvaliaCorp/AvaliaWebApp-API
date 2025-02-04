package com.avaliacorp.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.avaliacorp.api.models.CommentModel;

public interface CommentRepository extends CrudRepository<CommentModel, Integer> {

    List<CommentModel> findByPostId(Integer postId);//Procura vários comentários pelo Id do Post do comentário (Comentários de um Post, Avaliação)

    List<CommentModel> findByCommentId(Integer commentId);//Procura vários comentários pelo CommentId do comentário (Comentários de um comentário)

}
