package com.linkout.post.mapper;

import com.linkout.post.dtos.PostResponse;
import com.linkout.post.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    public PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getImageUrl(),
                post.getAuthor().getUsername(),
                post.getCreateAt(),
                post.getUpdateAt()
        );
    }

    public List<PostResponse> toResponseList(List<Post> posts) {
        return posts.stream().map(this::toResponse).toList();
    }
}
