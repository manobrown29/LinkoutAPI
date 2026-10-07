package com.linkout.user.controller;

import com.linkout.user.dto.UserRequest;
import com.linkout.user.dto.UserResponse;
import com.linkout.user.dto.UserUpdateRequest;
import com.linkout.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "CRUD de usuários")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping()
    @Operation(summary = "Cria um novo usuário")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(201).body(service.create(request));
    }

    @GetMapping
    @Operation(summary = "Listar todos usuários")
    public List<UserResponse> findAll(){ return service.findAll(); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um usuário")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um usuário")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest userRequest){
        return service.update(id, userRequest);
    }

    @PostMapping("/{id}/profile-image")
    @Operation( summary = "Envia/atualizar a foto de perfil do usuário")
    public UserResponse uploadImage(@PathVariable Long id,
                                    @RequestPart("image") MultipartFile file){
        return service.uploadProfileImage(id, file);
    }
}
