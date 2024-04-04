package com.gsalles.carrental.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsalles.carrental.dto.UsuarioDTO;
import com.gsalles.carrental.dto.UsuarioPasswordDTO;
import com.gsalles.carrental.entity.Usuario;
import com.gsalles.carrental.dto.mappers.UsuarioMapper;
import com.gsalles.carrental.dto.rdto.UsuarioResponseDTO;
import com.gsalles.carrental.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
	
	private final UsuarioService service;
	
	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> create(@RequestBody @Valid UsuarioDTO usuarioDTO){
		Usuario usuario = service.salvar(UsuarioMapper.toUsuario(usuarioDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(usuario));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENTE') AND #id == authentication.principal.id)")
	public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id){
		return ResponseEntity.ok(UsuarioMapper.toDto(service.buscarPorId(id)));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') AND #id == authentication.principal.id")
	public ResponseEntity<Void> updatePassword(@PathVariable Long id,
			@Valid @RequestBody UsuarioPasswordDTO senhaDto){
		service.alterarSenha(id, senhaDto.getSenhaAtual(), senhaDto.getNovaSenha(), 
				senhaDto.getConfirmarSenha());
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UsuarioResponseDTO>> findAll(){
		return ResponseEntity.ok(UsuarioMapper.toListDto(service.buscarTodos()));
	}
}
