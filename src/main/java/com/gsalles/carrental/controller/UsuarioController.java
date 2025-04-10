package com.gsalles.carrental.controller;

import com.gsalles.carrental.dto.UsuarioDTO;
import com.gsalles.carrental.dto.UsuarioPasswordDTO;
import com.gsalles.carrental.dto.mappers.UsuarioMapper;
import com.gsalles.carrental.dto.rdto.UsuarioResponseDTO;
import com.gsalles.carrental.entity.Usuario;
import com.gsalles.carrental.exception.EntityNotFoundException;
import com.gsalles.carrental.exception.PasswordInvalidException;
import com.gsalles.carrental.exception.UsernameUniqueViolationException;
import com.gsalles.carrental.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
	
	private final UsuarioService usuarioService;
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@Operation(
			summary = "Criar um usuário",
			description = "Operação para criar usuário",
			tags = { "Usuarios" },
			responses = {
					@ApiResponse(
							description = "Success",
							responseCode = "201",
							content = {
									@Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class)),
									@Content(mediaType = "application/xml", schema = @Schema(implementation = UsuarioResponseDTO.class))
							}
					),
					@ApiResponse(
							description = "Usuário já existe",
							responseCode = "409",
							content = {
									@Content(mediaType = "application/json", schema = @Schema(implementation = UsernameUniqueViolationException.class)),
									@Content(mediaType = "application/xml", schema = @Schema(implementation = UsernameUniqueViolationException.class))
							}
					),
					@ApiResponse(
							description = "Username ou senha inválidos.",
							responseCode = "422",
							content = {
							@Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidException.class)),
							@Content(mediaType = "application/xml", schema = @Schema(implementation = MethodArgumentNotValidException.class))
						}
					)
			}

	)
	public ResponseEntity<UsuarioResponseDTO> create(@RequestBody @Valid UsuarioDTO usuarioDTO){
		Usuario usuario = usuarioService.salvar(UsuarioMapper.toUsuario(usuarioDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(usuario));
	}
	
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENTE') AND #id == authentication.principal.id)")
	@Operation(
			summary = "Encontrar usuário por id",
			description = "Operação para encontrar usuário por ID. Requer autenticação com bearer token.",
			security = @SecurityRequirement(name = "security"),
			tags = {"Usuarios"},
			responses = {
					@ApiResponse(
							description = "Success",
							responseCode = "200",
							content = {
									@Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class)),
									@Content(mediaType = "application/xml", schema = @Schema(implementation = UsuarioResponseDTO.class))
							}
					),
					@ApiResponse(
							description = "Usuário não encontrado",
							responseCode = "404",
							content = {
									@Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class)),
									@Content(mediaType = "application/xml", schema = @Schema(implementation = EntityNotFoundException.class))
							}
					) ,
					@ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
					@ApiResponse(description = "Usuário sem permissão", responseCode = "403")
			}
	)
	public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id){
		UsuarioResponseDTO r = UsuarioMapper.toDto(usuarioService.buscarPorId(id));
		r.add(linkTo(methodOn(UsuarioController.class).findAll(0, 10, "id", "asc")).withRel("Lista de usuários"));
		return ResponseEntity.ok(r);
	}
	
	@PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') AND #id == authentication.principal.id")
	@Operation(
			summary = "Alterar senha de usuário",
			description = "Operação para alterar usuário senha por id. Requer autenticação com bearer token.",
			security = @SecurityRequirement(name = "security"),
			tags = {"Usuarios"},
			responses = {
					@ApiResponse(
							description = "Senha alterada com sucesso",
							responseCode = "204"
					),
					@ApiResponse(
							description = "Falha na alteração",
							responseCode = "400",
							content = {
									@Content(mediaType = "application/json", schema = @Schema(implementation = PasswordInvalidException.class)),
									@Content(mediaType = "application/xml", schema = @Schema(implementation = PasswordInvalidException.class))
							}
					),
					@ApiResponse(
							description = "A senha deve ter de 6 a 8 caracteres.",
							responseCode = "422",
							content = {
									@Content(mediaType = "application/json", schema = @Schema(implementation = PasswordInvalidException.class)),
									@Content(mediaType = "application/xml", schema = @Schema(implementation = PasswordInvalidException.class))
							}
					),
					@ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
					@ApiResponse(description = "Usuário sem permissão", responseCode = "403")
			}
	)
	public ResponseEntity<Void> updatePassword(@PathVariable Long id,
			@Valid @RequestBody UsuarioPasswordDTO senhaDto){
		usuarioService.alterarSenha(id, senhaDto.getSenhaAtual(), senhaDto.getNovaSenha(),
				senhaDto.getConfirmarSenha());
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(
			summary = "Encontrar todos usuários",
			description = "Operação para encontrar todos usuários. Requer autenticação com bearer token e permissão de admin.",
			tags = {"Usuarios"},
			parameters = {
					@Parameter(name = "page", description = "Número da página a ser retornada (começa em 0)", example = "0"),
					@Parameter(name = "size", description = "Quantidade de registros por página", example = "10"),
					@Parameter(name = "sortBy", description = "Campo para ordenação", example = "id"),
					@Parameter(name = "direction", description = "Direção da ordenação: asc ou desc", example = "asc")
			},
			security = @SecurityRequirement(name = "security"),
			responses = {
					@ApiResponse(
							description = "Success",
							responseCode = "200",
							content = {
									@Content(mediaType = "application/json", schema = @Schema(type = "array",
											implementation = UsuarioResponseDTO.class)),
									@Content(mediaType = "application/xml", schema = @Schema(type = "array",
											implementation = UsuarioResponseDTO.class))
							}
					),
					@ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
					@ApiResponse(description = "Usuário sem permissão de Admin", responseCode = "403")
			}
	)
	public ResponseEntity<Page<UsuarioResponseDTO>> findAll(@RequestParam(defaultValue = "0") int page,
															@RequestParam(defaultValue = "10") int size,
															@RequestParam(defaultValue = "id") String sortBy,
															@RequestParam(defaultValue = "asc") String direction){
		Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<UsuarioResponseDTO> list = UsuarioMapper.toListDto(usuarioService.buscarTodos(pageable));
		list.forEach(dto -> dto.add(linkTo(methodOn(UsuarioController.class).findById(dto.getId())).withRel("Self")));
		return ResponseEntity.ok(list);
	}
}
