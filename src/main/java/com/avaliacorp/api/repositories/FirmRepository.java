package com.avaliacorp.api.repositories;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.avaliacorp.api.models.FirmModel;

import java.util.List;
import java.util.Optional;


public interface FirmRepository extends CrudRepository<FirmModel, String> {

    Optional<FirmModel> findByCNPJ(String cNPJ);

    @Query("SELECT * FROM empresa WHERE email = :email")
    Optional<FirmModel> findByEmail(@Param("email") String email);

    List<FirmModel> findManyByName(String name);

    @Query("SELECT * FROM empresa WHERE nome LIKE :name LIMIT :limit")
    List<FirmModel> findManyByName(@Param("name") String name, @Param("limit") Integer limit);

}
