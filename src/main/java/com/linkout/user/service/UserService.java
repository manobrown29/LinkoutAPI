package com.linkout.user.service;

import com.linkout.storage.StorageService;
import com.linkout.user.dto.UserRequest;
import com.linkout.user.dto.UserResponse;
import com.linkout.user.dto.UserUpdateRequest;
import com.linkout.user.mapper.UserMapper;
import com.linkout.user.model.User;
import com.linkout.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final StorageService storageService;

    public UserService(UserRepository userRepository, UserMapper userMapper, StorageService storageService){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.storageService = storageService;
    }

    public List<UserResponse> findAll(){
        return userMapper.toResponseList(userRepository.findAll());
    }

    public UserResponse create(UserRequest request){
    if(userRepository.existsByEmail(request.email())){
        throw new RuntimeException("Email já está em uso");
    }

    if (userRepository.existsByUsername(request.username())){
        throw new RuntimeException("Username já está em uso");
    }
        User user = User.builder()
                .username(request.username())
                .name(request.name())
                .email(request.email())
                .bio(request.bio())
                .build();

        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UserUpdateRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        if(user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())){
            throw new RuntimeException("Email já está em uso");
        }

        user.setBio(request.bio());
        user.setEmail(request.email());
        user.setName(request.name());

        return userMapper.toResponse(userRepository.save(user));
    }

    public void delete(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        userRepository.delete(user);
    }

    public UserResponse uploadProfileImage(Long id, MultipartFile file){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        String url = storageService.upload(file, "avatars");

        user.setProfileImageUrl(url);

        return userMapper.toResponse(userRepository.save(user));
    }
}
