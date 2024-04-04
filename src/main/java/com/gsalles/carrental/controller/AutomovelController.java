package com.gsalles.carrental.controller;

import com.gsalles.carrental.dto.AutomovelDTO;
import com.gsalles.carrental.dto.mappers.AutomovelMapper;
import com.gsalles.carrental.dto.rdto.AutomovelResponseDTO;
import com.gsalles.carrental.entity.Automovel;
import com.gsalles.carrental.service.AutomovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/automoveis")
public class AutomovelController {

    private final AutomovelService service;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutomovelResponseDTO> create(@RequestBody @Valid AutomovelDTO dto){
        Automovel automovel = service.salvar(AutomovelMapper.toAutomovel(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(AutomovelMapper.toDto(automovel));
    }

    @GetMapping("/placa/{placa}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<AutomovelResponseDTO> findByPlaca(@PathVariable String placa){
        Automovel automovel = service.buscarPorPlaca(placa);
        return ResponseEntity.ok(AutomovelMapper.toDto(automovel));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<AutomovelResponseDTO> findById(@PathVariable Long id){
        Automovel automovel = service.buscarPorId(id);
        return ResponseEntity.ok(AutomovelMapper.toDto(automovel));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<List<AutomovelResponseDTO>> findAll(){
        List<Automovel> list = service.buscarTodos();
        return ResponseEntity.ok(AutomovelMapper.toListDto(list));
    }
}
