package com.avaliacorp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avaliacorp.api.exceptions.ForbiddenActionException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.CommentModel;
import com.avaliacorp.api.models.UserModel;
import com.avaliacorp.api.repositories.CommentRepository;
import com.avaliacorp.api.repositories.PostRepository;
import com.avaliacorp.api.repositories.UserRepository;

@Service//Define pro String que é um Service, também sub-entende que é um componente
public class CommentService {//Serviço que opera os comentários (comments)

    //Definido repositórios
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    //Construtor, inicializando os repositórios (como é componente, quem faz isso é o spring)
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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

    @Transactional//Criar comentário
    public CommentModel create(CommentModel data, String authorId){
        //Verifica se a string é null, se não manda um erro
        verifyString(authorId, "AuthorId param must not be null");

        //Verifica se PostId e CommentId são nulos, no máximo um pode ser null
        if(data.getPostId() == null && data.getCommentId() == null){
            throw new IllegalArgumentException("PostId or CommentId param must not be null");
        }

        //Verifica se os dois são válidos, somente 1 pode ser válido
        if(data.getPostId() != null && data.getCommentId() != null){
            throw new ForbiddenActionException("You cannot set both PostId and CommentId");
        }

        if(data.getPostId() != null){
            var post = postRepository.findById(data.getPostId()).orElseThrow(() -> new NotFoundException("The Post was not found"));
            if(post.getStatus()){
                throw new ForbiddenActionException("Cannot comment a closed post");
            }
        }
        else {
            commentRepository.findById(data.getCommentId()).orElseThrow(() -> new NotFoundException("The Comment was not found"));
        }

        //Finalmente, salva o comentário
        data.setAuthorId(authorId);
        return commentRepository.save(data);
    }

    @Transactional//Procura comentário pelo id
    public CommentModel findById(Integer id){
        //Verifica se o id é null
        verifyInteger(id, "Id param must not be null");

        //Procura o comentário pelo id
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("The Comment was not found"));
    }

    @Transactional//Procura vários comentários pelo id de um Post
    public List<CommentModel> findByPostId(Integer id){
        verifyInteger(id, "PostId param must not be null");

        postRepository.findById(id).orElseThrow(() -> new NotFoundException("The Post was not found"));

        return commentRepository.findByPostId(id);
    }

    @Transactional//Procura vários comentários pelo id de Comment
    public List<CommentModel> findByCommentId(Integer id){
        verifyInteger(id, "CommentId param must not be null");

        commentRepository.findById(id);

        return commentRepository.findByCommentId(id);
    }

    @Transactional//Atualiza um comentário, recebe o id do comentário a ser atualizado, o id do autor do comentário e o texto novo
    public CommentModel update(Integer id, String authorId, String text){
        //Verificações de sempre
        verifyInteger(id, "Id param must not be null");
        verifyString(authorId, "AuthorId param must not be null");
        verifyString(text, "Text param must not be null");

        //Verifica se as chaves estrengeiras apontam para ids que existem
        CommentModel comment = findById(id);
        UserModel user = userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("The User was not found"));

        //Se o id do user passado nos parametros existe, mas não for igual ao do comment encontrado, lança um erro (significa que é o usuário errado)
        if(!user.getId().equals(comment.getAuthorId())){
            throw new ForbiddenActionException("Forbidden, user does not own comment");
        }

        //Finalmente, salva as mudanças
        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Transactional//Deleta um comentário, recebe o id do comentário e o id do autor do comentário
    public void delete(Integer id, String authorId){
        verifyInteger(id, "Id param must not be null");
        verifyString(authorId, "AuthorId must not be null");

        UserModel user = userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("The User was not found"));
        CommentModel comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("The Comment was not found"));

        //Se o id do user passado nos parametros existe, mas não for igual ao do comment encontrado, lança um erro (significa que é o usuário errado)
        if(!user.getId().equals(comment.getAuthorId()) && !user.getRole().equals("Admin")){
            throw new ForbiddenActionException("The User does not own the comment");
        }

        //Finalmente, deleta o comentário
        commentRepository.deleteById(id);
    }
}
