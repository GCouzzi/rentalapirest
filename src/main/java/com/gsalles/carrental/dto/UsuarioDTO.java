package com.gsalles.carrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioDTO {
	
	@NotBlank(message = "Username é obrigatório")
	@Size(min = 8, max = 20, message = "Username deve conter entre 8 e 20 caracteres")
	private String username;
	
	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 6, max = 8, message = "Senha deve conter entre 6 e 8 caracteres")
	private String password;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
	private String nomeCompleto;

    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
	private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "Telefone deve conter 10 ou 11 dígitos"
    )
	private String telefone;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
	private String email;
}
