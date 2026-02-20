package com.gsalles.carrental.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "username", nullable = false, unique = true, length = 100)
	private String username;
	
	@Column(name = "password", nullable = false, length = 200)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 25)
	private Role role = Role.ROLE_CLIENTE;

    @Column(name = "nome_completo", nullable = false, length = 100)
    private String nomeCompleto;

    @Column(name = "cpf", nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(name = "telefone", nullable = false, length = 15, unique = true)
    private String telefone;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

	@CreatedDate
	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;

	@LastModifiedDate
	@Column(name = "data_modificacao")
	private LocalDateTime dataModificacao;

	@CreatedBy
	@Column(name = "criado_por")
	private String criadoPor;

	@LastModifiedBy
	@Column(name = "modificado_por")
	private String modificadoPor;
	
	public enum Role {
		ROLE_ADMIN, ROLE_CLIENTE
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
	
}
