package com.avaliacorp.api.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.avaliacorp.api.exceptions.ForbiddenActionException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.LikeModel;
import com.avaliacorp.api.models.PostModel;
import com.avaliacorp.api.models.UserModel;
import com.avaliacorp.api.repositories.FirmRepository;
import com.avaliacorp.api.repositories.LikeRepository;
import com.avaliacorp.api.repositories.PostRepository;
import com.avaliacorp.api.repositories.UserRepository;

@Service//Define pro String que é um Service, também sub-entende que é um componente
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FirmRepository firmRepository;
    private final LikeRepository likeRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, FirmRepository firmRepository, LikeRepository likeRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.firmRepository = firmRepository;
        this.likeRepository = likeRepository;
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

    public PostModel create(PostModel post, String authorId) throws IllegalArgumentException{
        if(post.getFkCNPJ() != null && !post.getFkCNPJ().isEmpty()){
            firmRepository.findByCNPJ(post.getFkCNPJ()).orElseThrow(() -> new NotFoundException("The firm CNPJ was not found"));
        }

        userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("The Author id was not found"));

        post.setAuthorId(authorId);
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    public PostModel findById(Integer id){
        if(id == null) throw new IllegalArgumentException("id param must not be null");
        return postRepository.findById(id).orElseThrow(() -> new NotFoundException("The post was not found.")); 
    }

    public List<PostModel> searchByAuthorId(String authorId){
        if(authorId == null) throw new IllegalArgumentException("authorId param must not be null");
        return postRepository.findByAuthorId(authorId);
    }

    public List<PostModel> findByFirmCNPJ(String cnpj){
        if(cnpj == null) throw new IllegalArgumentException("cnpj param must not be null");
        firmRepository.findByCNPJ(cnpj).orElseThrow(() -> new NotFoundException("The CNPJ was not found"));
        return postRepository.findByFkCNPJ(cnpj);
    }

    public List<PostModel> searchByTitle(String title, Integer limit){
        if(title == null) throw new IllegalArgumentException("title param must not be null");
        return postRepository.findByTitle(title, limit);
    }

    public void incrementLike(Integer id, String userId){
        //Verifica se não é nulo
        verifyInteger(id, "Id param must not be null");
        verifyString(userId, "UserId param must not be null");

        //Verifica se existe
        postRepository.findById(id).orElseThrow(() -> new NotFoundException("The Post was not found"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("The User was not found"));

        LikeModel like = new LikeModel(userId, id, null);

        //Verifica se o like já existe (Se o usuário já deu like)
        var alreadyLiked = likeRepository.findUnique(like);
        if(!(alreadyLiked == null)){
            throw new ForbiddenActionException("Forbidden, the user already liked the post");
        }

        //Por fim, salva
        likeRepository.save(like);
    }

    public void dislike(Integer id, String userId){
        verifyInteger(id, "Id param must not be null");
        verifyString(userId, "UserId param must not be null");

        postRepository.findById(id).orElseThrow(() -> new NotFoundException("The Post was not found"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("The User was not found"));

        LikeModel like = new LikeModel(userId, id, null);

        var isLiked = likeRepository.findUnique(like);
        verifyString(isLiked.getUserId(), "User does not liked the post");

        likeRepository.delete(like);
    }

    //Reecebe o id do autor do post e o id do post
    public void delete(String authorId, Integer id){
        //verifica se os parametros são nulos, se sim manda um erro
        if(authorId.isEmpty()) throw new IllegalArgumentException("How did we get here?");
        if(id == null) throw new IllegalArgumentException("id param must not be null");

        //verifica se o autor do post existe procurando ele pelo id; se não o achar, manda um erro
        UserModel user = userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("The user was not found (How did we get here?)"));

        //verifica se o post existe procurando ele pelo id; se não o achar, manda um erro
        PostModel post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("The post was not found"));

        //Se o id usuário não for igual ao id do autor do post, mandará um erro de ação proibida
        if(!user.getId().equals(post.getAuthorId())) throw new ForbiddenActionException("The user is not the author of the post");

        //Finalmente, se nenhum dos erros anteriores ocorreu o post será deletado
        postRepository.deleteById(id);
    }

}
