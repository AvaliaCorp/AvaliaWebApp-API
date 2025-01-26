package com.avaliacorp.api.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.avaliacorp.api.models.PostModel;

public interface PostRepository extends CrudRepository<PostModel, Integer> {

    @Query("SELECT * FROM avaliacao WHERE empresa_cnpj = :fkCNPJ")
    List<PostModel> findByFkCNPJ(@Param("fkCNPJ") String fkCNPJ);

    List<PostModel> findByAuthorId(String authorId);

    @Query("SELECT * FROM avaliacao WHERE titulo LIKE :title LIMIT :limit")
    List<PostModel> findByTitle(@Param("title") String title, @Param("limit") Integer limit);

}
