package com.gsalles.carrental.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AluguelUtils {

    public static String gerarRecibo(){
        LocalDateTime ldt = LocalDateTime.now();
        String recibo = ldt.toString().substring(0,19);
        return recibo
                .replace(":", "")
                .replace("T", "-")
                .replace("-", "");
    }

    public static BigDecimal calcularValor(LocalDateTime dataInicio, LocalDateTime dataFim) {
        long minutes = dataInicio.until(dataFim, ChronoUnit.MINUTES);
        double total = minutes * 0.45;
        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calcularDesconto(BigDecimal valor, long totalVezes) {
        if(totalVezes % 5 == 0){
            BigDecimal desconto = valor.multiply(new BigDecimal(0.15));
            return desconto.setScale(2, RoundingMode.HALF_EVEN);
        }
        return new BigDecimal(0);
    }
}
