package com.gsalles.carrental.dto.rdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AluguelResponseDTO {

    private String usuarioUsername;
    private String automovelMarca;
    private String automovelModelo;
    private String automovelCor;
    private String automovelPlaca;
    private String automovelRecibo;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime dataInicio;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime dataFim;
    private BigDecimal valor;
    private BigDecimal desconto;
}
