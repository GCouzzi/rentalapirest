package com.gsalles.carrental.dto.rdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {

	private Long id;
	private String username;
	private String role;
    private String nomeCompleto;
    private String cpf;
    private String telefone;
    private String email;
}
