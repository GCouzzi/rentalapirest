package com.gsalles.carrental.service;

import com.gsalles.carrental.entity.UsuarioAutomovel;
import com.gsalles.carrental.exception.EntityNotFoundException;
import com.gsalles.carrental.repository.UsuarioAutomovelRepository;
import com.gsalles.carrental.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioAutomovelService {
    private final UsuarioAutomovelRepository repository;
    private final UsuarioService uService;
    @Transactional
    public UsuarioAutomovel salvar(UsuarioAutomovel ua){
        return repository.save(ua);
    }

    @Transactional(readOnly = true)
    public UsuarioAutomovel buscarPorRecibo(String recibo) {
        return repository.findByRecibo(recibo).orElseThrow(
                () -> new EntityNotFoundException("Recibo não encontrado.")
        );
    }

    @Transactional(readOnly = true)
    public long buscarTotalDeAlugueis(String username) {
        return repository.countByUsuarioUsernameAndDataFimIsNotNull(username);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioAutomovel> buscarTodosAlugueisPorUsername(String username, Pageable pageable){
        uService.buscarPorUsername(username);
        return repository.findAllByUsuarioUsername(username, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioAutomovel> buscarTodos(Pageable pageable){
        return repository.findAll(pageable);
    }
}
