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

    @NotBlank
    @Size(min = 8, max = 20)
    private String usuarioUsername;
    @NotBlank
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}")
    @Size(min = 8, max = 8)
    private String automovelPlaca;
}
