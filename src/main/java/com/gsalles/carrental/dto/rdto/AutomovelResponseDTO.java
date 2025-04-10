package com.gsalles.carrental.dto.rdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutomovelResponseDTO extends RepresentationModel<AutomovelResponseDTO> {
    private String marca;
    private String modelo;
    private String cor;
    private String placa;
    private String status;
    private BigDecimal valorPorMinuto;
}
