package com.gsalles.carrental.dto.mappers;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.gsalles.carrental.dto.UsuarioDTO;
import com.gsalles.carrental.entity.Usuario;
import com.gsalles.carrental.dto.rdto.UsuarioResponseDTO;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsuarioMapper {
	
	public static Usuario toUsuario(UsuarioDTO dto) {
		return new ModelMapper().map(dto, Usuario.class);
	}
	
	public static UsuarioResponseDTO toDto(Usuario usuario) {
		String role = usuario.getRole().name().substring("ROLE_".length());
		PropertyMap<Usuario, UsuarioResponseDTO> props = new PropertyMap<Usuario, UsuarioResponseDTO>(){
			@Override
			protected void configure() {
				map().setRole(role);
			}
		};
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(props);
		return mapper.map(usuario, UsuarioResponseDTO.class);
	}
	
	public static Page<UsuarioResponseDTO> toListDto(Page<Usuario> list){
		return list.map(UsuarioMapper::toDto);
	}
    public static List<UsuarioResponseDTO> toListDto(List<Usuario> list){
        return list.stream().map(UsuarioMapper::toDto).collect(Collectors.toList());
    }
}
