package com.linkout.post.service;

import com.linkout.post.dtos.PostRequest;
import com.linkout.post.dtos.PostResponse;
import com.linkout.post.dtos.PostUpdateRequest;
import com.linkout.post.mapper.PostMapper;
import com.linkout.post.model.Post;
import com.linkout.post.repository.PostRepository;
import com.linkout.user.model.User;
import com.linkout.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAll() {
        return postMapper.toResponseList(postRepository.findAll());
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Long id) {
        return postMapper.toResponse(findPost(id));
    }

    public PostResponse create(PostRequest request) {
        User author = findAuthor(request.authorId());
        Post post = Post.builder()
                .content(requireContent(request.content()))
                .author(author)
                .build();
        return postMapper.toResponse(postRepository.save(post));
    }

    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = findPost(id);
        post.setContent(requireContent(request.content()));
        return postMapper.toResponse(postRepository.save(post));
    }

    public void delete(Long id) {
        postRepository.delete(findPost(id));
    }

    private Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicação não encontrada"));
    }

    private User findAuthor(Long authorId) {
        return userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado"));
    }

    private String requireContent(String content) {
        if (content == null || content.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O conteúdo da publicação é obrigatório");
        }
        return content;
    }
}
