package com.gsalles.carrental.controller;

import com.gsalles.carrental.dto.UsuarioDTO;
import com.gsalles.carrental.entity.Usuario;
import com.gsalles.carrental.exception.ErrorMessage;
import com.gsalles.carrental.jwt.JwtToken;
import com.gsalles.carrental.jwt.JwtUtils;
import com.gsalles.carrental.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    private final AuthenticationManager manager;
    private final UsuarioService service;
    @PostMapping("/auth")
    public ResponseEntity<?> authentication(@RequestBody @Valid UsuarioDTO usuarioDto, HttpServletRequest request){
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    usuarioDto.getUsername(), usuarioDto.getPassword());
            manager.authenticate(authenticationToken);
            Usuario.Role role = service.buscarRolePorUsername(usuarioDto.getUsername());
            JwtToken token = JwtUtils.createToken(usuarioDto.getUsername(), role.name().substring("ROLE_".length()));
            return ResponseEntity.ok(token);
        } catch(AuthenticationException ex){
            log.error(ex.getMessage());
            log.warn("Bad credentials from username {}", usuarioDto.getUsername());
        }
        return ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais inválidas."));
    }
}
