package com.gsalles.carrental.service;

import com.gsalles.carrental.entity.Usuario;
import com.gsalles.carrental.exception.EntityNotFoundException;
import com.gsalles.carrental.exception.PasswordInvalidException;
import com.gsalles.carrental.exception.UsernameUniqueViolationException;
import com.gsalles.carrental.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
	
	private final UsuarioRepository repository;
	private final PasswordEncoder encoder;
	@Transactional
	public Usuario salvar(Usuario usuario) {
		try {
			usuario.setPassword(encoder.encode(usuario.getPassword()));
			return repository.save(usuario);
		} catch(DataIntegrityViolationException ex) {
			throw new UsernameUniqueViolationException("Esse username já existe.");
		}
	}
	
	@Transactional(readOnly=true)
	public Usuario buscarPorId(Long id) {
		return repository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Id não encontrado.")
				);
	}

	@Transactional
	public void alterarSenha(Long id, String senhaAtual, String novaSenha, String confirmarSenha) {
		Usuario user = buscarPorId(id);
		if(!encoder.matches(senhaAtual, user.getPassword())) {
			throw new PasswordInvalidException("Senha atual não confere.");
		} else if(!novaSenha.equals(confirmarSenha)) {
			throw new PasswordInvalidException("Nova senha não confere com confirmação.");
		} else if(encoder.matches(novaSenha, user.getPassword())) {
			throw new PasswordInvalidException("Nova senha idêntica à senha atual.");
		}
		user.setPassword(encoder.encode(novaSenha));
	}

	@Transactional(readOnly = true)
	public Page<Usuario> buscarTodos(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
		return repository.findByUsername(username).orElseThrow(
				() -> new EntityNotFoundException("Username não encontrado.")
		);
    }

	@Transactional(readOnly = true)
    public Usuario.Role buscarRolePorUsername(String username) {
		return repository.findRoleByUsername(username);
    }
}
