package com.gsalles.carrental.controller;

import com.gsalles.carrental.dto.AluguelDTO;
import com.gsalles.carrental.dto.mappers.AluguelMapper;
import com.gsalles.carrental.dto.rdto.AluguelResponseDTO;
import com.gsalles.carrental.dto.rdto.UsuarioResponseDTO;
import com.gsalles.carrental.entity.UsuarioAutomovel;
import com.gsalles.carrental.exception.AluguelAutomovelViolationException;
import com.gsalles.carrental.exception.EntityNotFoundException;
import com.gsalles.carrental.jwt.JwtUserDetails;
import com.gsalles.carrental.service.AluguelService;
import com.gsalles.carrental.service.AutomovelService;
import com.gsalles.carrental.service.UsuarioAutomovelService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("api/v1/alugueis")
@RequiredArgsConstructor
public class AluguelController {
    
    private final AluguelService aluguelService;
    private final UsuarioAutomovelService usuarioAutomovelService;
    private final AutomovelService automovelService;
    private final UsuarioService usuarioService;

    @PostMapping(value = "/checkin", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Realizar check-in",
            description = "Operação para realizar checkin. Necessita de permissão ADMIN.",
            tags = {"Aluguel"},
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            description = "Checkin realizado com sucesso",
                            responseCode = "201",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelResponseDTO.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AluguelResponseDTO.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Usuário não encontrado",
                            responseCode = "404",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = EntityNotFoundException.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Automóvel não encontrado",
                            responseCode = "404",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = EntityNotFoundException.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Automóvel já alugado",
                            responseCode = "409",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelAutomovelViolationException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AluguelAutomovelViolationException.class))
                            }
                    ),
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
                    @ApiResponse(description = "Usuário sem permissão de Admin", responseCode = "403")
            }
    )
    public ResponseEntity<AluguelResponseDTO> checkin(@RequestBody @Valid AluguelDTO dto){
        UsuarioAutomovel ua = AluguelMapper.toUsuarioAutomovel(dto);
        ua.setUsuario(usuarioService.buscarPorUsername(ua.getUsuario().getUsername()));
        ua.setAutomovel(automovelService.buscarPorPlaca(ua.getAutomovel().getPlaca()));
        AluguelResponseDTO rDto = AluguelMapper.toDto(aluguelService.checkIn(ua));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{recibo}")
                .buildAndExpand(ua.getRecibo()).toUri();
        rDto.add(linkTo(methodOn(AluguelController.class).getByRecibo(rDto.getAutomovelRecibo())).withSelfRel());
        return ResponseEntity.created(location).body(rDto);
    }

    @GetMapping(value = "/{recibo}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Busca de aluguel por recibo",
            description = "Operação para buscar operação de aluguel por recibo. Requer permissão de admin",
            tags = {"Aluguel"},
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            description = "Operação de aluguel encontrada",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelResponseDTO.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AluguelResponseDTO.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Operação de aluguel não encontrada",
                            responseCode = "404",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = EntityNotFoundException.class))
                            }
                    ),
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
                    @ApiResponse(description = "Usuário sem permissão de Admin", responseCode = "403")
            }
    )
    public ResponseEntity<AluguelResponseDTO> getByRecibo(@PathVariable String recibo){
        UsuarioAutomovel ua = usuarioAutomovelService.buscarPorRecibo(recibo);
        AluguelResponseDTO rDto = AluguelMapper.toDto(ua);
        rDto.add(linkTo(methodOn(AluguelController.class).getAllAlugueisByUsername(rDto.getUsuarioUsername(), 0,
                10, "id", "asc")).withRel("Lista de aluguéis"));
        return ResponseEntity.ok(rDto);
    }

    @Operation(
            summary = "Operação de checkout",
            description = "Operação para realizar checkout do aluguel. Requer autorização de admin",
            tags = {"Aluguel"},
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            description = "Operação de checkout concluída com sucesso",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelResponseDTO.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AluguelResponseDTO.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Nenhum aluguel foi encontrado com esse recibo",
                            responseCode = "404",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = EntityNotFoundException.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Aluguel com esse recibo já realizou checkout",
                            responseCode = "409",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelAutomovelViolationException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AluguelAutomovelViolationException.class))
                            }
                    ),
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
                    @ApiResponse(description = "Usuário sem permissão de Admin", responseCode = "403")
            }
    )
    @GetMapping(value = "/checkout/{recibo}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AluguelResponseDTO> checkout(@PathVariable String recibo){
        UsuarioAutomovel ua = aluguelService.checkout(recibo);
        AluguelResponseDTO rDto = AluguelMapper.toDto(ua);
        rDto.add(linkTo(methodOn(AluguelController.class).getByRecibo(rDto.getAutomovelRecibo())).withSelfRel());
        return ResponseEntity.ok(rDto);
    }

    @Operation(
            summary = "Operação de listar todos os aluguéis do usuário autenticado.",
            description = "Operação para listar aluguéis do usuário autenticado. Requer autenticação.",
            tags = {"Aluguel"},
            parameters = {
                    @Parameter(name = "page", description = "Número da página a ser retornada (começa em 0)", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de registros por página", example = "10"),
                    @Parameter(name = "sortBy", description = "Campo para ordenação", example = "id"),
                    @Parameter(name = "direction", description = "Direção da ordenação: asc ou desc", example = "asc")
            },
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(description = "Operação de listagem realizada com sucesso", responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelResponseDTO.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AluguelResponseDTO.class)),
                            }
                    ),
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401")
            }
    )
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<Page<AluguelResponseDTO>> getAllAlugueis(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                                   @RequestParam(defaultValue = "asc") String direction){
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AluguelResponseDTO> list = AluguelMapper.toListDto(usuarioAutomovelService.buscarTodosAlugueisPorUsername(userDetails.getUsername(), pageable));
        list.forEach(dto -> dto.add(linkTo(methodOn(AluguelController.class).getByRecibo(dto.getAutomovelRecibo())).withRel("Self")));
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Operação de listar todos os aluguéis de usuário por username.",
            description = "Operação para listar aluguéis de usuário por username. Requer permissão de admin.",
            tags = {"Aluguel"},
            parameters = {
                    @Parameter(name = "page", description = "Número da página a ser retornada (começa em 0)", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de registros por página", example = "10"),
                    @Parameter(name = "sortBy", description = "Campo para ordenação", example = "id"),
                    @Parameter(name = "direction", description = "Direção da ordenação: asc ou desc", example = "asc")
            },
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            description = "Operação de listagem realizada com sucesso",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelResponseDTO.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AluguelResponseDTO.class))
                            }
                    ),
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
                    @ApiResponse(description = "Usuário sem permissão de Admin", responseCode = "403"),
                    @ApiResponse(
                            description = "Usuário não encontrado",
                            responseCode = "404",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = EntityNotFoundException.class))
                            }
                    )
            }
    )
    @GetMapping(value = "/username/{username}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AluguelResponseDTO>> getAllAlugueisByUsername(@PathVariable String username,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                                             @RequestParam(defaultValue = "asc") String direction){
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AluguelResponseDTO> list = AluguelMapper.toListDto(usuarioAutomovelService.buscarTodosAlugueisPorUsername(username, pageable));
        list.forEach(dto -> dto.add(linkTo(methodOn(AluguelController.class).getByRecibo(dto.getAutomovelRecibo())).withRel("Self")));
        return ResponseEntity.ok(list);
    }
}
