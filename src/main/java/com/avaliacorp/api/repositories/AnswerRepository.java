package com.avaliacorp.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.avaliacorp.api.models.AnswerModel;
import java.util.List;


public interface AnswerRepository extends CrudRepository<AnswerModel, Integer> {

    List<AnswerModel> findByCnpj(String cnpj);//Procura várias Respostas pelo CNPJ (a chave estrangeira)

    List<AnswerModel> findByPostId(Integer postId);//Procura várias Respostas pelo PostId (a chave estrangeira)

}
