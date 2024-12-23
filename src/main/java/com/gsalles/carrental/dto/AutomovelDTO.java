package com.gsalles.carrental.dto;

import com.gsalles.carrental.entity.Automovel;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class AutomovelDTO {
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @NotBlank
    private String cor;
    @NotBlank
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}")
    @Size(min = 8, max = 8)
    private String placa;
    @Positive
    private BigDecimal valorPorMinuto;
}
