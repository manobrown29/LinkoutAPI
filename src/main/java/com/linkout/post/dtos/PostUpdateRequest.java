package com.linkout.post.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @NotBlank(message = "O conteúdo da publicação é obrigatório")
        @Size(max = 3000, message = "O conteúdo deve ter no máximo 3000 caracteres")
        String content
) {
}
