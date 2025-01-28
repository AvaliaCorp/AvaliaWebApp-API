package com.avaliacorp.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.avaliacorp.api.models.CommentModel;

public interface CommentRepository extends CrudRepository<CommentModel, Integer> {

    List<CommentModel> findByPostId(Integer postId);

    List<CommentModel> findByCommentId(Integer commentId);
}
