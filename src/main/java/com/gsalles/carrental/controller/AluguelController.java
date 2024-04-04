package com.gsalles.carrental.controller;

import com.gsalles.carrental.dto.AluguelDTO;
import com.gsalles.carrental.dto.mappers.AluguelMapper;
import com.gsalles.carrental.dto.rdto.AluguelResponseDTO;
import com.gsalles.carrental.entity.UsuarioAutomovel;
import com.gsalles.carrental.jwt.JwtUserDetails;
import com.gsalles.carrental.service.AluguelService;
import com.gsalles.carrental.service.AutomovelService;
import com.gsalles.carrental.service.UsuarioAutomovelService;
import com.gsalles.carrental.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/alugueis")
@RequiredArgsConstructor
public class AluguelController {
    private final AluguelService alService;
    private final UsuarioAutomovelService uaService;
    private final AutomovelService aService;
    private final UsuarioService uService;

    @PostMapping("/checkin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AluguelResponseDTO> checkin(@RequestBody @Valid AluguelDTO dto){
        UsuarioAutomovel ua = AluguelMapper.toUsuarioAutomovel(dto);
        ua.setUsuario(uService.buscarPorUsername(ua.getUsuario().getUsername()));
        ua.setAutomovel(aService.buscarPorPlaca(ua.getAutomovel().getPlaca()));
        AluguelResponseDTO rDto = AluguelMapper.toDto(alService.checkIn(ua));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{recibo}")
                .buildAndExpand(ua.getRecibo()).toUri();
        return ResponseEntity.created(location).body(rDto);
    }

    @GetMapping("/{recibo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AluguelResponseDTO> getByRecibo(@PathVariable String recibo){
        UsuarioAutomovel ua = uaService.buscarPorRecibo(recibo);
        AluguelResponseDTO rDto = AluguelMapper.toDto(ua);
        return ResponseEntity.ok(rDto);
    }

    @GetMapping("/checkout/{recibo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AluguelResponseDTO> checkout(@PathVariable String recibo){
        UsuarioAutomovel ua = alService.checkout(recibo);
        AluguelResponseDTO rDto = AluguelMapper.toDto(ua);
        return ResponseEntity.ok(rDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<List<AluguelResponseDTO>> getAllAlugueis(@AuthenticationPrincipal JwtUserDetails userDetails){
        List<AluguelResponseDTO> list = AluguelMapper.toListDto(uaService.buscarTodosAlugueisPorUsername(userDetails.getUsername()));
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AluguelResponseDTO>> getAllAlugueisByUsername(@PathVariable String username){
        List<AluguelResponseDTO> list = AluguelMapper.toListDto(uaService.buscarTodosAlugueisPorUsername(username));
        return ResponseEntity.ok(list);
    }
}
