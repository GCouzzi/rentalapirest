package com.gsalles.carrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "automoveis")
public class Automovel {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "marca", nullable = false)
	private String marca;
	@Column(name = "modelo", nullable = false)
	private String modelo;
	@Column(name = "cor", nullable = false)
	private String cor;
	@Column(name = "placa", nullable = false, unique = true)
	private String placa;
	@Column(name = "valor_por_minuto", columnDefinition = "decimal(8,2)")
	private BigDecimal valorPorMinuto;
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.LIVRE;
	public enum Status{
		LIVRE, ALUGADO
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Automovel automovel = (Automovel) o;
		return Objects.equals(id, automovel.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
