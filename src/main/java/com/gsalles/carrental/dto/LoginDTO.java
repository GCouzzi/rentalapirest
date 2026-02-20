package com.gsalles.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record LoginDTO(@NotBlank(message = "Username é obrigatório")
                       @Size(min = 8, max = 20, message = "Username deve conter entre 8 e 20 caracteres")
                       String username,
                       @NotBlank(message = "Senha é obrigatória")
                       @Size(min = 6, max = 8, message = "Senha deve conter entre 6 e 8 caracteres")
                       String password) {
}
