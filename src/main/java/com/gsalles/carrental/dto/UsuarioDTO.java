package com.gsalles.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioDTO {
	
	@NotBlank
	@Size(min = 8, max = 20)
	private String username;
	
	@NotBlank
	@Size(min = 6, max = 8)
	private String password;
}
