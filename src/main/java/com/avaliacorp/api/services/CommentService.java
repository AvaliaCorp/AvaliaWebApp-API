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

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

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

    @Transactional
    public CommentModel create(CommentModel data, String authorId){
        verifyString(authorId, "AuthorId param must not be null");
        if(data.getPostId() == null && data.getCommentId() == null){
            throw new IllegalArgumentException("PostId or CommentId param must not be null");
        }
        if(data.getPostId() != null && data.getCommentId() != null){
            throw new ForbiddenActionException("You cannot set both PostId and CommentId");
        }

        data.setAuthorId(authorId);
        return commentRepository.save(data);
    }

    @Transactional
    public CommentModel findById(Integer id){
        verifyInteger(id, "Id param must not be null");

        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("The Comment was not found"));
    }

    @Transactional
    public List<CommentModel> findByPostId(Integer id){
        verifyInteger(id, "PostId param must not be null");
        postRepository.findById(id).orElseThrow(() -> new NotFoundException("The Post was not found"));
        return commentRepository.findByPostId(id);
    }

    @Transactional
    public List<CommentModel> findByCommentId(Integer id){
        verifyInteger(id, "CommentId param must not be null");
        commentRepository.findById(id);
        return commentRepository.findByCommentId(id);
    }

    @Transactional
    public CommentModel update(Integer id, String authorId, String text){
        verifyInteger(id, "Id param must not be null");
        verifyString(authorId, "AuthorId param must not be null");
        verifyString(text, "Text param must not be null");

        CommentModel comment = findById(id);
        UserModel user = userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("The User was not found"));

        if(!user.getId().equals(comment.getAuthorId())){
            throw new ForbiddenActionException("Forbidden, user does not own comment");
        }

        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Transactional
    public void delete(Integer id, String authorId){
        verifyInteger(id, "Id param must not be null");
        verifyString(authorId, "AuthorId must not be null");

        UserModel user = userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("The User was not found"));
        CommentModel comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("The Comment was not found"));

        if(!user.getId().equals(comment.getAuthorId())){
            throw new ForbiddenActionException("The User does not own the comment");
        }

        commentRepository.deleteById(id);
    }
}
