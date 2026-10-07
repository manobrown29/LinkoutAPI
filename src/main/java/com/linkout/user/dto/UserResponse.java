package com.linkout.user.dto;

public record UserResponse (
        Long id,
        String name,
        String email,
        String username,
        String profileImageUrl,
        String bio
){
}
