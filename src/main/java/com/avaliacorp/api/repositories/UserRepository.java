package com.avaliacorp.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.avaliacorp.api.models.UserModel;

public interface UserRepository extends CrudRepository<UserModel, String> {

    Optional<UserModel> findByEmail(String email);//Procura por usuário pelo email

    @Query("SELECT * FROM usuario WHERE nome LIKE :name LIMIT :limit")//Pesquisa por vários usuários pelo nome com um limite definido
    List<UserModel> findManyByName(@Param("name") String name,@Param("limit") Integer limit);

}
