package com.gsalles.carrental.service;

import com.gsalles.carrental.entity.Automovel;
import com.gsalles.carrental.exception.AutomovelUniqueViolationException;
import com.gsalles.carrental.exception.EntityNotFoundException;
import com.gsalles.carrental.repository.AutomovelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutomovelService {

    private final AutomovelRepository repository;

    @Transactional
    public Automovel salvar(Automovel automovel) {
        if(repository.findByPlaca(automovel.getPlaca()).isPresent()){
            throw new AutomovelUniqueViolationException("Placa já registrada");
        }
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

    public Page<Automovel> buscarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
