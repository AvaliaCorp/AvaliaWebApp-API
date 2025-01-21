package com.avaliacorp.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.avaliacorp.api.models.PostModel;

public interface PostRepository extends CrudRepository<PostModel, Integer> {

    List<PostModel> findByFkCNPJ(String fkCNPJ);

    List<PostModel> findByAuthorId(String authorId);

}
