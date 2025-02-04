package com.avaliacorp.api.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.avaliacorp.api.models.PostModel;

public interface PostRepository extends CrudRepository<PostModel, Integer> {

    @Query("SELECT * FROM avaliacao WHERE empresa_cnpj = :fkCNPJ")//Procura vários posts pela foreign key empresa_cnpj
    List<PostModel> findByFkCNPJ(@Param("fkCNPJ") String fkCNPJ);

    List<PostModel> findByAuthorId(String authorId);//Procura vários posts pelo Id do Autor

    @Query("SELECT * FROM avaliacao WHERE titulo LIKE :title LIMIT :limit")//Pesquisa vários posts pelo titulo com um limite definido
    List<PostModel> findByTitle(@Param("title") String title, @Param("limit") Integer limit);

    @Query("SELECT MAX(nota) FROM avaliacao WHERE empresa_cnpj = :cnpj")
    Double getMaxGradeWithCNPJFilter(@Param("cnpj") String cnpj);

    @Query("SELECT MIN(nota) FROM avaliacao WHERE empresa_cnpj = :cnpj")
    Double getMinGradeWithCNPJFilter(@Param("cnpj") String cnpj);

}
