package com.avaliacorp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.AnswerModel;
import com.avaliacorp.api.models.FirmModel;
import com.avaliacorp.api.repositories.AnswerRepository;
import com.avaliacorp.api.repositories.FirmRepository;
import com.avaliacorp.api.repositories.PostRepository;

@Service//Define pro String que é um Service, também sub-entende que é um componente
public class AnswerService {//Serviço que opera as Respostas (Answers)

    //Repositórios utilizados
    private final AnswerRepository answerRepository;
    private final PostRepository postRepository;
    private final FirmRepository firmRepository;

    //Construtor, inicializando os repositórios (como é componente, quem faz isso é o spring)
    public AnswerService(AnswerRepository answerRepository, PostRepository postRepository, FirmRepository firmRepository){
        this.answerRepository = answerRepository;
        this.postRepository = postRepository;
        this.firmRepository = firmRepository;
    }

    private void verifyString(String str, String errorMessage) throws RuntimeException {
        if(str.isEmpty() || str == null || str.isBlank()){
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void verifyInteger(Integer num, String errorMessage) throws RuntimeException {
        if(num == null){
            throw new IllegalArgumentException(errorMessage);
        }
    }

    //@Transactional faz com que o spring entenda que método faz uma operção com banco de dados e que se algo der errado ele corte tudo na hora sem deixar vestígios
    @Transactional//Método para criar uma resposta
    public AnswerModel create(AnswerModel data, String firmId){
        //Verifica se os argumentos necessários não são nulos ou vazias, se forem, lança um erro com a mensagem passado.
        verifyString(firmId, "FirmId must not be null");
        verifyInteger(data.getPostId(), "PostId must not be null");

        //Verifica se o post da resposta e se o id da firma passa existem, se não lança um erro personalizado com as mensagens:
        postRepository.findById(data.getPostId()).orElseThrow(() -> new NotFoundException("The Post was not found"));
        FirmModel firm = firmRepository.findById(firmId).orElseThrow(() -> new NotFoundException("The Firm was not found"));

        //Se chegou até aqui, é porque nenhum erro ocorreu, já pode salvar a resposta
        data.setCnpj(firm.getCNPJ());//Seta o cnpj da resposta como o da empresa que foi encontrada
        return answerRepository.save(data);
    }

    public List<AnswerModel> findByPostId(Integer postId){
        //Verifica se postId é null
        verifyInteger(postId, "PostId param must not be null");

        //Verifica se o post existe, se não lança um erro
        postRepository.findById(postId).orElseThrow(() -> new NotFoundException("The Post was not found"));

        //Finalmente, procura as respostas que tenham o postId
        return answerRepository.findByPostId(postId);
    }


}
