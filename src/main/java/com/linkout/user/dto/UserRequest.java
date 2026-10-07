package com.linkout.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest (
        @Size(min = 3, max = 30, message = "O username deve ter 3 a 30 caracteres")
        String username,

        @Size(max = 100)
        String name,

        @Size(max = 500)
        String bio,

        @Size(max = 150)
        @Email(message = "E-mail invalido")
        @NotBlank(message = "E-mail é obrigatório")
        String email
){
}
