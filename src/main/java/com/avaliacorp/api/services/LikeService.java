package com.avaliacorp.api.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.LikeModel;
import com.avaliacorp.api.repositories.CommentRepository;
import com.avaliacorp.api.repositories.LikeRepository;
import com.avaliacorp.api.repositories.PostRepository;
import com.avaliacorp.api.repositories.UserRepository;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

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

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository){
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public LikeModel create(String userId, Integer postId, Integer commentId){
        verifyString(userId, "UserId param must not be null");

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("The User does not exist"));
        if(postId != null && commentId != null)
            throw new IllegalArgumentException("Both postId and commentId can not be defined, thou must choose only one");
        else if(postId != null)
            postRepository.findById(postId).orElseThrow(() -> new NotFoundException("The Post does not exist"));
        else if(commentId != null)
            commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("The Comment does not exist"));
        else
            throw new IllegalArgumentException("Both postId and commentId can not be null, thou must choose one of them");

        LikeModel like = new LikeModel(userId, postId, commentId);
        return likeRepository.save(like);
    }

    public Integer countByPost(Integer postId){
        verifyInteger(postId, "PostId param must not be null");
        postRepository.findById(postId).orElseThrow(() -> new NotFoundException("The Post does not exist"));
        return likeRepository.countByPost(postId);
    }

    public Integer countByComment(Integer commentId){
        verifyInteger(commentId, "CommentId param must not be null");
        commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("The Comment does not exist"));
        return likeRepository.countByComment(commentId);
    }

    @Transactional
    public void delete(String userId, Integer postId, Integer commentId){
        verifyString(userId, "UserId param must not be null");
        verifyInteger(postId, "PostId param must not be null");
        verifyInteger(commentId, "CommentId param must not be null");

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("The User does not exist"));
        if(postId != null)
            postRepository.findById(postId).orElseThrow(() -> new NotFoundException("The Post does not exist"));
        else
            commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("The Comment does not exist"));

        LikeModel like = new LikeModel(userId, postId, commentId);
        likeRepository.delete(like);
    }
}
