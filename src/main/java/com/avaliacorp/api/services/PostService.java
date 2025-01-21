package com.avaliacorp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.PostModel;
import com.avaliacorp.api.repositories.FirmRepository;
import com.avaliacorp.api.repositories.PostRepository;
import com.avaliacorp.api.repositories.UserRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FirmRepository firmRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, FirmRepository firmRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.firmRepository = firmRepository;
    }

    public PostModel create(PostModel post) throws IllegalArgumentException{
        if(post.getAuthorId() == null) throw new IllegalArgumentException("Author id must not be null");
        if(post.getFkCNPJ() == null) throw new IllegalArgumentException("Firm's CNPJ musts not be null");
        userRepository.findById(post.getAuthorId()).orElseThrow(() -> new NotFoundException("The Author id was not found"));
        firmRepository.findByCNPJ(post.getFkCNPJ()).orElseThrow(() -> new NotFoundException("The firm CNPJ was not found"));

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

    public List<PostModel> searchByFirmCNPJ(String cnpj){
        if(cnpj == null) throw new IllegalArgumentException("cnpj param must not be null");
        return postRepository.findByFkCNPJ(cnpj);
    }

}
