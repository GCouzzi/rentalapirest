package com.gsalles.carrental.dto.rdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutomovelResponseDTO {
    private String marca;
    private String modelo;
    private String cor;
    private String placa;
    private String status;

}
