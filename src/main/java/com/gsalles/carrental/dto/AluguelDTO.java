package com.gsalles.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AluguelDTO {

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 8, max = 20, message = "Username deve possuir entre 8 e 20 caracteres")
    private String usuarioUsername;
    @NotBlank(message = "Placa é obrigatório")
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "Placa deve atender ao padrão AAA-0000")
    @Size(min = 8, max = 8, message = "Placa deve possuir 8 caracteres")
    private String automovelPlaca;
}
