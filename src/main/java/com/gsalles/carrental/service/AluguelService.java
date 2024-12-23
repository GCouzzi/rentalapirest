package com.gsalles.carrental.service;

import com.gsalles.carrental.entity.Automovel;
import com.gsalles.carrental.entity.Usuario;
import com.gsalles.carrental.entity.UsuarioAutomovel;
import com.gsalles.carrental.exception.AluguelAutomovelViolationException;
import com.gsalles.carrental.utils.AluguelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AluguelService {

    private final UsuarioAutomovelService uaService;
    private final UsuarioService uService;
    private final AutomovelService aService;

    @Transactional
    public UsuarioAutomovel checkIn(UsuarioAutomovel ua){
        Automovel automovel = aService.buscarPorPlaca(ua.getAutomovel().getPlaca());
        if(automovel.getStatus() == Automovel.Status.ALUGADO){
            throw new AluguelAutomovelViolationException("Esse automóvel já está alugado.");
        }
        ua.setAutomovel(automovel);
        automovel.setStatus(Automovel.Status.ALUGADO);

        Usuario usuario = uService.buscarPorUsername(ua.getUsuario().getUsername());
        ua.setUsuario(usuario);
        ua.setDataInicio(LocalDateTime.now());
        long totalVezes = uaService.buscarTotalDeAlugueis(ua.getUsuario().getUsername());
        ua.setRecibo(AluguelUtils.gerarRecibo());

        return uaService.salvar(ua);
    }

    @Transactional
    public UsuarioAutomovel checkout(String recibo){
        UsuarioAutomovel ua = uaService.buscarPorRecibo(recibo);
        if(ua.getAutomovel().getStatus() != Automovel.Status.ALUGADO){
            throw new AluguelAutomovelViolationException("Recibo já realizou checkout.");
        }
        ua.setDataFim(LocalDateTime.now());
        ua.getAutomovel().setStatus(Automovel.Status.LIVRE);
        long totalVezes = uaService.buscarTotalDeAlugueis(ua.getUsuario().getUsername());
        ua.setValor(AluguelUtils.calcularValor(ua.getDataInicio(), ua.getDataFim(), ua.getAutomovel().getValorPorMinuto()));
        ua.setDesconto(AluguelUtils.calcularDesconto(ua.getValor(), totalVezes));
        return uaService.salvar(ua);
    }
}
