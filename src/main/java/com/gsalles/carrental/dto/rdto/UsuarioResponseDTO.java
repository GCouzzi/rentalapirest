package com.gsalles.carrental.dto.rdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioResponseDTO {

	private String id;
	private String username;
	private String role;
}
