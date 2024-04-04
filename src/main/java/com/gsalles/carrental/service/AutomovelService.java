package com.gsalles.carrental.service;

import com.gsalles.carrental.entity.Automovel;
import com.gsalles.carrental.exception.EntityNotFoundException;
import com.gsalles.carrental.repository.AutomovelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutomovelService {

    private final AutomovelRepository repository;

    @Transactional
    public Automovel salvar(Automovel automovel) {
        return repository.save(automovel);
    }

    public Automovel buscarPorPlaca(String placa) {
        return repository.findByPlaca(placa).orElseThrow(
                () -> new EntityNotFoundException("Placa não encontrada.")
        );
    }

    public Automovel buscarPorId(Long id){
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Id não encontrado.")
        );
    }

    public List<Automovel> buscarTodos() {
        return repository.findAll();
    }
}
