package com.gsalles.carrental.controller;

import com.gsalles.carrental.dto.LoginDTO;
import com.gsalles.carrental.dto.UsuarioDTO;
import com.gsalles.carrental.dto.rdto.UsuarioResponseDTO;
import com.gsalles.carrental.entity.Usuario;
import com.gsalles.carrental.exception.ErrorMessage;
import com.gsalles.carrental.jwt.JwtToken;
import com.gsalles.carrental.jwt.JwtUtils;
import com.gsalles.carrental.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    @PostMapping(value = "/auth", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(
            summary = "Autenticar usuario",
            description = "Operação para autenticar usuário",
            tags = {"Usuarios"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtToken.class)),
                                    @Content(mediaType = "application/ml", schema = @Schema(implementation = JwtToken.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Falha na autenticação",
                            responseCode = "400",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)),
                                    @Content(mediaType = "application/xml", schema = @Schema(implementation = ErrorMessage.class))
                            }
                    )
            }
    )
    public ResponseEntity<?> authentication(@RequestBody @Valid LoginDTO loginDto, HttpServletRequest request){
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDto.username(), loginDto.password());
            authenticationManager.authenticate(authenticationToken);
            Usuario.Role role = usuarioService.buscarRolePorUsername(loginDto.username());
            JwtToken token = JwtUtils.createToken(loginDto.username(), role.name().substring("ROLE_".length()));
            return ResponseEntity.ok(token);
        } catch(AuthenticationException ex){
            log.error(ex.getMessage());
            log.warn("Bad credentials from username {}", loginDto.username());
        }
        return ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais inválidas."));
    }
}
