package com.avaliacorp.api.services;

import org.springframework.stereotype.Service;

import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.repositories.CommentRepository;
import com.avaliacorp.api.repositories.FirmRepository;
import com.avaliacorp.api.repositories.PostRepository;
import com.avaliacorp.api.repositories.UserRepository;

@Service
public class AdminService extends UserService {

    private final CommentRepository commentRepository;
    private final FirmRepository firmRepository;

    public AdminService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, FirmRepository firmRepository){
        super(userRepository, postRepository);
        this.commentRepository = commentRepository;
        this.firmRepository = firmRepository;
    }

    public void deleteFirm(String firmId){
        firmRepository.findById(firmId).orElseThrow(() -> new NotFoundException("The Firm was not found"));
        firmRepository.deleteById(firmId);
    }

    public void closePost(Integer postId){
        var post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("The Post was not found"));
        post.setStatus(true);
        postRepository.save(post);
    }

    public void deletePost(Integer postId){
        postRepository.findById(postId).orElseThrow(() -> new NotFoundException("The Post was not found"));
        postRepository.deleteById(postId);
    }

    public void deleteComment(Integer commentId){
        commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("The Comment was not found"));
        commentRepository.deleteById(commentId);
    }
}
