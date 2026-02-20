package com.gsalles.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioPasswordDTO {
	
	@NotBlank(message = "Senha atual é obrigatório")
	@Size(min = 6, max = 8, message = "Senha deve possuir entre 6 a 8 caracteres")
	private String senhaAtual;
	
	@NotBlank(message = "Nova senha é obrigatório")
	@Size(min = 6, max = 8, message = "Senha deve possuir entre 6 a 8 caracteres")
	private String novaSenha;
	
	@NotBlank(message = "Confirmar senha é obrigatório")
	@Size(min = 6, max = 8, message = "Senha deve possuir entre 6 a 8 caracteres")
	private String confirmarSenha;
}
