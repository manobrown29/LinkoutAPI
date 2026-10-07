package com.linkout.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest (
        @Size(max = 100)
        @NotBlank(message = "Name é obrigatório")
        String name,

        @Size(max = 500)
        String bio,

        @Size(max = 150)
        @Email(message = "E-mail invalido")
        @NotBlank(message = "E-mail é obrigatório")
        String email
){
}
