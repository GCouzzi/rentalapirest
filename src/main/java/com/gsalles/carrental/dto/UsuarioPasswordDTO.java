package com.gsalles.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioPasswordDTO {
	
	@NotBlank
	@Size(min = 6, max = 8)
	private String senhaAtual;
	
	@NotBlank
	@Size(min = 6, max = 8)
	private String novaSenha;
	
	@NotBlank
	@Size(min = 6, max = 8)
	private String confirmarSenha;
}
