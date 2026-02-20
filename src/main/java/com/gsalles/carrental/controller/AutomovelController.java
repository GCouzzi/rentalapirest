package com.gsalles.carrental.controller;

import com.gsalles.carrental.dto.AutomovelDTO;
import com.gsalles.carrental.dto.mappers.AutomovelMapper;
import com.gsalles.carrental.dto.rdto.AutomovelResponseDTO;
import com.gsalles.carrental.dto.rdto.UsuarioResponseDTO;
import com.gsalles.carrental.entity.Automovel;
import com.gsalles.carrental.exception.AutomovelUniqueViolationException;
import com.gsalles.carrental.exception.EntityNotFoundException;
import com.gsalles.carrental.exception.UsernameUniqueViolationException;
import com.gsalles.carrental.service.AutomovelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/automoveis")
public class AutomovelController {

    private final AutomovelService automovelService;

    @Operation(
            summary = "Criar um automóvel",
            description = "Operação para criar um automóvel",
            tags = { "Automoveis" },
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AutomovelResponseDTO.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AutomovelResponseDTO.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Automovel já existente.",
                            responseCode = "409",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AutomovelUniqueViolationException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AutomovelUniqueViolationException.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Valor por minuto deve ser positivo e a placa deve respeitar o padrão (XXX-0000)",
                            responseCode = "422",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AutomovelUniqueViolationException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AutomovelUniqueViolationException.class))
                            }
                    ),
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
                    @ApiResponse(description = "Usuário sem permissão", responseCode = "403")
            }
    )
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutomovelResponseDTO> create(@RequestBody @Valid AutomovelDTO dto){
        Automovel automovel = automovelService.salvar(AutomovelMapper.toAutomovel(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(AutomovelMapper.toDto(automovel));
    }

    @Operation(
            summary = "Buscar automóvel por placa",
            description = "Operação para buscar automóvel por placa",
            tags = { "Automoveis" },
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AutomovelResponseDTO.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AutomovelResponseDTO.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Automovel não encontrado.",
                            responseCode = "404",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = EntityNotFoundException.class))
                            }
                    )
                    ,
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
            }
    )
    @GetMapping(value = "/placa/{placa}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<AutomovelResponseDTO> findByPlaca(@PathVariable String placa){
        AutomovelResponseDTO dto = AutomovelMapper.toDto(automovelService.buscarPorPlaca(placa));
        dto.add(linkTo(methodOn(AutomovelController.class).findAll(0, 10, "id", "asc")).withRel("Lista de automóveis"));
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Buscar automóvel por id",
            description = "Operação para buscar automóvel por id",
            tags = { "Automoveis" },
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AutomovelResponseDTO.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = AutomovelResponseDTO.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Automovel não encontrado.",
                            responseCode = "404",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = EntityNotFoundException.class))
                            }
                    ),
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
            }
    )
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<AutomovelResponseDTO> findById(@PathVariable Long id){
        AutomovelResponseDTO dto = AutomovelMapper.toDto(automovelService.buscarPorId(id));
        dto.add(linkTo(methodOn(AutomovelController.class).findAll(0, 10, "id", "asc")).withRel("Lista de automóveis"));
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Listar todos automóveis",
            description = "Operação para listar todos os automóveis.",
            tags = { "Automoveis" },
            parameters = {
                    @Parameter(name = "page", description = "Número da página a ser retornada (começa em 0)", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de registros por página", example = "10"),
                    @Parameter(name = "sortBy", description = "Campo para ordenação", example = "id"),
                    @Parameter(name = "direction", description = "Direção da ordenação: asc ou desc", example = "asc")
            },
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(type = "array",
                                            implementation = AutomovelResponseDTO.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(type = "array",
                                            implementation = AutomovelResponseDTO.class))
                            }
                    ),
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401")
            }
    )
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Page<AutomovelResponseDTO>> findAll(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "asc") String direction){
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AutomovelResponseDTO> list = AutomovelMapper.toListDto(automovelService.buscarTodos(pageable));
        list.forEach(dto -> dto.add(linkTo(methodOn(AutomovelController.class).findByPlaca(dto.getPlaca())).withRel("Self")));
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Deletar automóvel por placa",
            description = "Operação deletar automóvel por placa",
            tags = { "Automoveis" },
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Automovel não encontrado.",
                            responseCode = "404",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = EntityNotFoundException.class))
                            }
                    ),
                    @ApiResponse(description = "Usuário não está autenticado.", responseCode = "401"),
                    @ApiResponse(description = "Usuário não possui permissão.", responseCode = "403")
            }
    )
    @DeleteMapping(value = "/placa/{placa}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteByPlaca(@PathVariable String placa){
        automovelService.deleteByPlaca(placa);
        return ResponseEntity.noContent().build();
    }
}
